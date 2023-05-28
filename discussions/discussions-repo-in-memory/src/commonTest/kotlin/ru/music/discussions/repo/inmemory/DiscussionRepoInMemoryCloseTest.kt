package ru.music.discussions.repo.inmemory

import ru.music.discussions.repo.tests.RepoDiscussionCloseTest


class DiscussionRepoInMemoryCloseTest : RepoDiscussionCloseTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}