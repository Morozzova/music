package ru.music.discussions.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import repo.DbDiscussionResponse
import repo.DbDiscussionsResponse
import ru.music.common.DiscContext
import ru.music.common.DiscCorSettings
import ru.music.common.models.*
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.repo.tests.DiscussionsRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoUsersDiscussionsTest {

    private val userId = DiscUserId("321")
    private val command = DiscCommand.USERS_DISCUSSIONS

    private val initDisc = DiscDiscussion(
        id = DiscId("123"),
        title = "abc",
        soundUrl = "abc",
        status = DiscStatus.OPEN,
        answers = mutableListOf(DiscAnswer("111")),
        ownerId = userId
    )

    private val initDisc2 = DiscDiscussion(
        id = DiscId("321"),
        title = "abc",
        soundUrl = "abc",
        status = DiscStatus.OPEN,
        answers = mutableListOf(DiscAnswer("222")),
        ownerId = userId
    )

    private val repo by lazy {
        DiscussionsRepositoryMock(
            invokeReadDiscussion = {
                DbDiscussionResponse(
                    isSuccess = true,
                    data = initDisc
                )
            },
            invokeUsersDiscussions = {
                DbDiscussionsResponse(
                    isSuccess = true,
                    data = listOf(initDisc, initDisc2)
                )
            }
        )
    }

    private val settings = DiscCorSettings(
        repoTest = repo
    )
    private val processor = DiscussionsProcessor(settings)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoUsersDiscussionsSuccessTest() = runTest {
        val ctx = DiscContext(
            command = command,
            state = DiscState.NONE,
            workMode = DiscWorkMode.TEST,
            multiDiscussionsRequest = DiscMulti(userId)
        )
        processor.exec(ctx)
        assertEquals(DiscState.FINISHING, ctx.state)
        assertEquals(userId, ctx.multiDiscussionsResponse.first().ownerId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoUsersDiscussionsNotFoundTest() = repoUsersIdNotFoundTest(command)
}
