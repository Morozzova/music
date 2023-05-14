package ru.music.discussions.repo.inmemory

import ru.music.discussions.repo.tests.RepoDiscussionReadTest

class DiscussionRepoInMemoryReadTest : RepoDiscussionReadTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
    )
}