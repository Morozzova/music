package ru.music.discussions.repo.inmemory

import ru.music.discussions.repo.tests.RepoDiscussionUpdateTest

class DiscussionRepoInMemoryUpdateTest : RepoDiscussionUpdateTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}