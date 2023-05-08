package ru.music.discussions.repo.inmemory

import RepoDiscussionUsersDiscussionsTest

class DiscussionRepoInMemoryUsersDiscussionsTest : RepoDiscussionUsersDiscussionsTest() {
    override val repo = DiscussionsRepoInMemory(
        initObjects = initObjects,
    )
}