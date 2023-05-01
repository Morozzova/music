package ru.music.discussions.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import repo.*
import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscError
import ru.music.common.models.DiscId
import ru.music.common.models.DiscUserId
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

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(ad: DiscDiscussion) {
        val entity = DiscussionEntity(ad)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        val key = randomUuid()
        val ad = rq.discussion.copy(id = DiscId(key))
        val entity = DiscussionEntity(ad)
        cache.put(key, entity)
        return DbDiscussionResponse(
            data = ad,
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

    override suspend fun updateDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        val key = rq.discussion.id.takeIf { it != DiscId.NONE }?.asString() ?: return resultErrorEmptyId
        val newAd = rq.discussion.copy()
        val entity = DiscussionEntity(newAd)
        return when (cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.put(key, entity)
                DbDiscussionResponse(
                    data = newAd,
                    isSuccess = true,
                )
            }
        }
    }

    override suspend fun closeDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        val key = rq.discussion.id.takeIf { it != DiscId.NONE }?.asString() ?: return resultErrorEmptyId
        val newAd = rq.discussion.copy()
        val entity = DiscussionEntity(newAd)
        return when (cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.put(key, entity)
                DbDiscussionResponse(
                    data = newAd,
                    isSuccess = true,
                )
            }
        }
    }

    override suspend fun deleteDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse {
        val key = rq.id.takeIf { it != DiscId.NONE }?.asString() ?: return resultErrorEmptyId
        return when (val oldAd = cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.invalidate(key)
                DbDiscussionResponse(
                    data = oldAd.toInternal(),
                    isSuccess = true,
                )
            }
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
    }
}
