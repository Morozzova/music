package ru.music.discussions.stubs

import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscUserId
import ru.music.discussions.stubs.DiscStubItems.ALL_DISCUSSIONS
import ru.music.discussions.stubs.DiscStubItems.DISCUSSION_FIRST
import ru.music.discussions.stubs.DiscStubItems.USERS_DISCUSSIONS

object DiscStub {
    fun get(): DiscDiscussion = DISCUSSION_FIRST.copy()
    fun getAll(): MutableList<DiscDiscussion> = ALL_DISCUSSIONS.toMutableList()
    fun getUsers(): MutableList<DiscDiscussion> = USERS_DISCUSSIONS.toMutableList()

    fun prepareResult(block: DiscDiscussion.() -> Unit): DiscDiscussion = get().apply(block)

    fun prepareDiscussionsList(id: DiscUserId?) = listOf(
        DISCUSSION_FIRST,
        DISCUSSION_FIRST,
        DISCUSSION_FIRST,
        DISCUSSION_FIRST
    )
}
