package ru.music.discussions.repo.inmemory

import ru.music.discussions.repo.tests.RepoDiscussionDeleteTest

class DiscussionRepoInMemoryDeleteTest : RepoDiscussionDeleteTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
    )
}