package ru.music.discussions.repo.inmemory

import RepoDiscussionReadTest

class DiscussionRepoInMemoryReadTest : RepoDiscussionReadTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
    )
}