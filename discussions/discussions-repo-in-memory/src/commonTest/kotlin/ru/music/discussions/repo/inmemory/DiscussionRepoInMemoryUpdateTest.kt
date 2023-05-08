package ru.music.discussions.repo.inmemory

import RepoDiscussionUpdateTest

class DiscussionRepoInMemoryUpdateTest : RepoDiscussionUpdateTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
    )
}