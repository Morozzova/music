package ru.music.discussions.repo.inmemory

import RepoDiscussionCloseTest

class DiscussionRepoInMemoryCloseTest : RepoDiscussionCloseTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}