package ru.music.discussions.repo.inmemory

import ru.music.discussions.repo.tests.RepoDiscussionUsersDiscussionsTest

class DiscussionRepoInMemoryUsersDiscussionsTest : RepoDiscussionUsersDiscussionsTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
    )
}