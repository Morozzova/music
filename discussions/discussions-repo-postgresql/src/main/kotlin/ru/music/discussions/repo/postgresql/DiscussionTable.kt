package ru.music.discussions.repo.postgresql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.music.common.models.*

object DiscussionTable : Table("discussions") {
    val id = varchar("id", 128)
    val title = varchar("title", 512)
    val soundUrl = varchar("sound_url", 512)
    val owner = varchar("owner", 128)
    var status = enumeration("status", DiscStatus::class)
    var answers = largeText("answers")
    val lock = varchar("lock", 50)

    override val primaryKey = PrimaryKey(id)

    fun from(res: InsertStatement<Number>) = DiscDiscussion(
        id = DiscId(res[id].toString()),
        title = res[title],
        soundUrl = res[soundUrl],
        ownerId = DiscUserId(res[owner].toString()),
        status = res[status],
        answers = res[answers].toDBAnswers(),
        lock = DiscLock(res[lock])
    )

    fun from(res: ResultRow) = DiscDiscussion(
        id = DiscId(res[id].toString()),
        title = res[title],
        soundUrl = res[soundUrl],
        ownerId = DiscUserId(res[owner].toString()),
        status = res[status],
        answers = res[answers].toDBAnswers(),
        lock = DiscLock(res[lock])
    )

    fun to(it: UpdateBuilder<*>, discussion: DiscDiscussion, randomUuid: () -> String) {
        it[id] = discussion.id.takeIf { it != DiscId.NONE }?.asString() ?: randomUuid()
        it[title] = discussion.title
        it[soundUrl] = discussion.soundUrl
        it[owner] = discussion.ownerId.asString()
        it[status] = discussion.status
        it[answers] = discussion.answers.toDBString()
        it[lock] = discussion.lock.takeIf { it != DiscLock.NONE }?.asString() ?: randomUuid()
    }
}