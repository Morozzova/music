package ru.music.discussions.repo.inmemory

import RepoDiscussionCreateTest

class DiscussionRepoInMemoryCreateTest : RepoDiscussionCreateTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}