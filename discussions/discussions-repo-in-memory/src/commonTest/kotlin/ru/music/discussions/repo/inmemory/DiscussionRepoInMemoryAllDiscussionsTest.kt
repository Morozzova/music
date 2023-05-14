package ru.music.discussions.repo.inmemory

import ru.music.discussions.repo.tests.RepoDiscussionAllDiscussionsTest

class DiscussionRepoInMemoryAllDiscussionsTest : RepoDiscussionAllDiscussionsTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
    )
}