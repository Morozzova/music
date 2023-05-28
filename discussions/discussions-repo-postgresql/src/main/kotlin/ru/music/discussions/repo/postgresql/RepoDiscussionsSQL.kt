package ru.music.discussions.repo.postgresql

import com.benasher44.uuid.uuid4
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import repo.*
import ru.music.common.helpers.asDiscError
import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscId
import ru.music.common.models.DiscLock
import ru.music.common.models.DiscUserId
import ru.music.common.repo.DbDiscussionUsersIdRequest

class RepoDiscussionsSQL(
    properties: SqlProperties,
    initObjects: Collection<DiscDiscussion> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() },
) : IDiscussionRepository {

    init {
        val driver = when {
            properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
            else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
        }

        Database.connect(
            properties.url, driver, properties.user, properties.password
        )

        transaction {
            if (properties.dropDatabase) SchemaUtils.drop(DiscussionTable)
            SchemaUtils.create(DiscussionTable)
            initObjects.forEach { createDiscussion(it) }
        }
    }

    private fun createDiscussion(discussion: DiscDiscussion): DiscDiscussion {
        val res = DiscussionTable.insert {
            to(it, discussion, randomUuid)
        }

        return DiscussionTable.from(res)
    }

    private fun <T> transactionWrapper(block: () -> T, handle: (Exception) -> T): T =
        try {
            transaction {
                block()
            }
        } catch (e: Exception) {
            handle(e)
        }

    private fun transactionWrapper(block: () -> DbDiscussionResponse): DbDiscussionResponse =
        transactionWrapper(block) { DbDiscussionResponse.error(it.asDiscError()) }

    override suspend fun createDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse = transactionWrapper {
        DbDiscussionResponse.success(createDiscussion(rq.discussion))
    }

    private fun read(id: DiscId): DbDiscussionResponse {
        val res = DiscussionTable.select {
            DiscussionTable.id eq id.asString()
        }.singleOrNull() ?: return DbDiscussionResponse.errorNotFound
        return DbDiscussionResponse.success(DiscussionTable.from(res))
    }

    override suspend fun readDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse =
        transactionWrapper { read(rq.id) }

    private fun update(
        id: DiscId,
        lock: DiscLock,
        block: (DiscDiscussion) -> DbDiscussionResponse
    ): DbDiscussionResponse =
        transactionWrapper {
            if (id == DiscId.NONE) return@transactionWrapper DbDiscussionResponse.errorEmptyId

            val current = DiscussionTable.select { DiscussionTable.id eq id.asString() }
                .firstOrNull()
                ?.let { DiscussionTable.from(it) }

            when {
                current == null -> DbDiscussionResponse.errorNotFound
                current.lock != lock -> DbDiscussionResponse.errorConcurrent(lock, current)
                else -> block(current)
            }
        }


    override suspend fun updateDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse =
        update(rq.discussion.id, rq.discussion.lock) {
            DiscussionTable.update({ DiscussionTable.id eq rq.discussion.id.asString() }) {
                to(it, rq.discussion.copy(lock = DiscLock(randomUuid())), randomUuid)
            }
            read(rq.discussion.id)
        }

    override suspend fun closeDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse =
        update(rq.discussion.id, rq.discussion.lock) {
            DiscussionTable.update({ DiscussionTable.id eq rq.discussion.id.asString() }) {
                to(it, rq.discussion.copy(lock = DiscLock(randomUuid())), randomUuid)
            }
            read(rq.discussion.id)
        }

    override suspend fun deleteDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse = update(rq.id, rq.lock) {
        DiscussionTable.deleteWhere { id eq rq.id.asString() }
        DbDiscussionResponse.success(it)
    }

    override suspend fun allDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse =
        transactionWrapper({
            val res = DiscussionTable.selectAll()
            DbDiscussionsResponse(data = res.map { DiscussionTable.from(it) }, isSuccess = true)
        }, {
            DbDiscussionsResponse.error(it.asDiscError())
        })

    override suspend fun usersDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse =
        transactionWrapper({
            val res = DiscussionTable.select {
                buildList {
                    add(Op.TRUE)
                    if (rq.usersId != DiscUserId.NONE && rq.usersId != null) {
                        add(DiscussionTable.owner eq rq.usersId!!.asString())
                    }
                }.reduce { a, b -> a and b }
            }
            DbDiscussionsResponse(data = res.map { DiscussionTable.from(it) }, isSuccess = true)
        }, {
            DbDiscussionsResponse.error(it.asDiscError())
        })
}
