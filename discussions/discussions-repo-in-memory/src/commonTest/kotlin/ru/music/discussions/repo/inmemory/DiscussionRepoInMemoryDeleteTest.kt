package ru.music.discussions.repo.inmemory

import RepoDiscussionDeleteTest

class DiscussionRepoInMemoryDeleteTest : RepoDiscussionDeleteTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
    )
}