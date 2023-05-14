package ru.music.discussions.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import repo.*
import ru.music.common.models.*
import ru.music.common.repo.DbDiscussionUsersIdRequest
import ru.music.discussions.repo.inmemory.model.DiscussionEntity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class DiscussionsRepoInMemory(
    initObjects: List<DiscDiscussion> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : IDiscussionRepository {

    private val cache = Cache.Builder<String, DiscussionEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(disc: DiscDiscussion) {
        val entity = DiscussionEntity(disc)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        val key = randomUuid()
        val disc = rq.discussion.copy(id = DiscId(key), lock = DiscLock(randomUuid()))
        val entity = DiscussionEntity(disc)
        cache.put(key, entity)
        return DbDiscussionResponse(
            data = disc,
            isSuccess = true,
        )
    }

    override suspend fun readDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse {
        val key = rq.id.takeIf { it != DiscId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbDiscussionResponse(
                    data = it.toInternal(),
                    isSuccess = true,
                )
            } ?: resultErrorNotFound
    }

    private suspend fun doUpdate(
        id: DiscId,
        oldLock: DiscLock,
        okBlock: (key: String, oldDisc: DiscussionEntity) -> DbDiscussionResponse
    ): DbDiscussionResponse {
        val key = id.takeIf { it != DiscId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLockStr = oldLock.takeIf { it != DiscLock.NONE }?.asString()
            ?: return resultErrorEmptyLock

        return mutex.withLock {
            val oldDisc = cache.get(key)
            when {
                oldDisc == null -> resultErrorNotFound
                oldDisc.lock != oldLockStr -> DbDiscussionResponse.errorConcurrent(
                    oldLock,
                    oldDisc.toInternal()
                )

                else -> okBlock(key, oldDisc)
            }
        }
    }

    override suspend fun updateDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        return doUpdate(rq.discussion.id, rq.discussion.lock) { key, _ ->
            val newDisc = rq.discussion.copy(lock = DiscLock(randomUuid()))
            val entity = DiscussionEntity(newDisc)
            cache.put(key, entity)
            DbDiscussionResponse.success(newDisc)
        }
    }

    override suspend fun closeDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        return doUpdate(rq.discussion.id, rq.discussion.lock) { key, _ ->
            val newDisc = rq.discussion.copy(lock = DiscLock(randomUuid()))
            val entity = DiscussionEntity(newDisc)
            cache.put(key, entity)
            DbDiscussionResponse.success(newDisc)
        }
    }

    override suspend fun deleteDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse {
        return doUpdate(rq.id, rq.lock) { key, oldDisc ->
            cache.invalidate(key)
            DbDiscussionResponse.success(oldDisc.toInternal())
        }
    }

    override suspend fun allDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse {
        val result = cache.asMap().map { it.value.toInternal() }.toList()
        return DbDiscussionsResponse(
            data = result,
            isSuccess = true,
        )
    }

    override suspend fun usersDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse {
        val result = cache.asMap()
            .filter { entry ->
                rq.usersId.takeIf { it != DiscUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .map { it.value.toInternal() }.toList()
        return DbDiscussionsResponse(
            data = result,
            isSuccess = true,
        )
    }


    companion object {
        val resultErrorEmptyId = DbDiscussionResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                DiscError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = DbDiscussionResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                DiscError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
        val resultErrorEmptyLock = DbDiscussionResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                DiscError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )
    }
}
