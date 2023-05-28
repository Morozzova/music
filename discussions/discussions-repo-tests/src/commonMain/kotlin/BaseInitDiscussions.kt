package ru.music.discussions.repo.tests

import ru.music.common.models.*

abstract class BaseInitDiscussions(val op: String): IInitObjects<DiscDiscussion> {

    open val lockOld: DiscLock = DiscLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: DiscLock = DiscLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: DiscUserId = DiscUserId("owner-555"),
        soundUrl: String = "sound",
        status: DiscStatus = DiscStatus.CLOSED,
        lock: DiscLock = lockOld,
        answers: MutableList<DiscAnswer> = mutableListOf()
    ) = DiscDiscussion(
        id = DiscId("discussion-repo-$op-$suf"),
        title = "$suf stub",
        soundUrl = soundUrl,
        ownerId = ownerId,
        status = status,
        answers = answers,
        lock = lock
    )
}
