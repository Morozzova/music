package ru.music.discussions.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import permissions.MusicPrincipalModel
import permissions.MusicUserGroups
import repo.DbDiscussionResponse
import repo.DbDiscussionsResponse
import ru.music.common.DiscContext
import ru.music.common.DiscCorSettings
import ru.music.common.models.*
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.repo.tests.DiscussionsRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoAllDiscussionsTest {

    private val userId = DiscUserId("321")
    private val command = DiscCommand.ALL_DISCUSSIONS

    private val initDisc = DiscDiscussion(
        id = DiscId("123"),
        title = "abc",
        soundUrl = "abc",
        status = DiscStatus.OPEN,
        answers = mutableListOf(DiscAnswer("111")),
    )

    private val repo by lazy {
        DiscussionsRepositoryMock(
            invokeReadDiscussion = {
                DbDiscussionResponse(
                    isSuccess = true,
                    data = initDisc
                )
            },
            invokeAllDiscussions = {
                DbDiscussionsResponse(
                    isSuccess = true,
                    data = listOf(initDisc)
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
    fun repoAllDiscussionsSuccessTest() = runTest {
        val ctx = DiscContext(
            command = command,
            state = DiscState.NONE,
            workMode = DiscWorkMode.TEST,
            multiDiscussionsRequest = DiscMulti(null),
            principal = MusicPrincipalModel(
                id = userId,
                groups = setOf(
                    MusicUserGroups.USER,
                    MusicUserGroups.TEST,
                )
            ),
        )
        processor.exec(ctx)
        assertEquals(DiscState.FINISHING, ctx.state)
        assertEquals(1, ctx.multiDiscussionsResponse.size)
    }
}
