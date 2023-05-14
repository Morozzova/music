package ru.music.discussions.repo.postgresql

import RepoDiscussionAllDiscussionsTest
import RepoDiscussionCloseTest
import RepoDiscussionCreateTest
import RepoDiscussionDeleteTest
import RepoDiscussionReadTest
import RepoDiscussionUpdateTest
import RepoDiscussionUsersDiscussionsTest
import repo.IDiscussionRepository

class RepoDiscussionsSQLCreateTest : RepoDiscussionCreateTest() {
    override val repo: IDiscussionRepository = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoDiscussionsSQLDeleteTest : RepoDiscussionDeleteTest() {
    override val repo: IDiscussionRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoDiscussionsSQLReadTest : RepoDiscussionReadTest() {
    override val repo: IDiscussionRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoDiscussionsSQLUpdateTest : RepoDiscussionUpdateTest() {
    override val repo: IDiscussionRepository = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoDiscussionsSQLCloseTest : RepoDiscussionCloseTest() {
    override val repo: IDiscussionRepository = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoDiscussionsSQLAllDiscussionsTest : RepoDiscussionAllDiscussionsTest() {
    override val repo: IDiscussionRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoDiscussionsSQLUsersDiscussionsTest : RepoDiscussionUsersDiscussionsTest() {
    override val repo: IDiscussionRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}
