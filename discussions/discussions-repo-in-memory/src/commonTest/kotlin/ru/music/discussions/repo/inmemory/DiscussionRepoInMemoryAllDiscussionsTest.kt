package ru.music.discussions.repo.inmemory

import RepoDiscussionAllDiscussionsTest

class DiscussionRepoInMemoryAllDiscussionsTest : RepoDiscussionAllDiscussionsTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
    )
}