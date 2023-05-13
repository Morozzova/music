package ru.music.discussions.repo.inmemory.model

import ru.music.common.models.*

data class DiscussionEntity(
    val id: String? = null,
    val title: String? = null,
    val soundUrl: String? = null,
    val ownerId: String? = null,
    val status: String? = null,
    val answers: MutableList<String> = mutableListOf(),
    val lock: String? = null,
) {
    constructor(model: DiscDiscussion): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        soundUrl = model.soundUrl.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        status = model.status.takeIf { it != DiscStatus.NONE }?.name,
        answers = model.answers.takeIf { it.isNotEmpty() }?.map { it.asString() }?.toMutableList() ?: mutableListOf(),
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = DiscDiscussion(
        id = id?.let { DiscId(it) }?: DiscId.NONE,
        title = title?: "",
        soundUrl = soundUrl?: "",
        ownerId = ownerId?.let { DiscUserId(it) }?: DiscUserId.NONE,
        status = status?.let { DiscStatus.valueOf(it) }?: DiscStatus.NONE,
        answers = answers.map { DiscAnswer(it) }.toMutableList(),
        lock = lock?.let { DiscLock(it) } ?: DiscLock.NONE,
    )
}
