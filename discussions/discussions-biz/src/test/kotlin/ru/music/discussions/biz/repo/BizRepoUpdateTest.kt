package ru.music.discussions.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import repo.DbDiscussionResponse
import ru.music.common.DiscContext
import ru.music.common.DiscCorSettings
import ru.music.common.models.*
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.repo.tests.DiscussionsRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoUpdateTest {
    private val userId = DiscUserId("321")
    private val command = DiscCommand.UPDATE

    private val initDisc = DiscDiscussion(
        id = DiscId("123"),
        title = "abc",
        soundUrl = "abc",
        status = DiscStatus.OPEN,
        answers = mutableListOf(DiscAnswer("111")),
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
            invokeUpdateDiscussion = {
                DbDiscussionResponse(
                    isSuccess = true,
                    data = DiscDiscussion(
                        id = DiscId("12345"),
                        title = "xyz",
                        soundUrl = "xyz",
                        status = DiscStatus.CLOSED,
                        answers = mutableListOf(DiscAnswer("000"), DiscAnswer("111"))
                    )
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
    fun repoUpdateSuccessTest() = runTest {
        val discToUpd = DiscDiscussion(
            id = DiscId("12345"),
            title = "xyz",
            soundUrl = "xyz",
            status = DiscStatus.CLOSED,
            answers = mutableListOf(DiscAnswer("000"), DiscAnswer("111")),
            lock = DiscLock("123-234-abc-ABC")
        )

        val ctx = DiscContext(
            command = command,
            state = DiscState.NONE,
            workMode = DiscWorkMode.TEST,
            discussionRequest = discToUpd
        )
        processor.exec(ctx)
        assertEquals(DiscState.FINISHING, ctx.state)
        assertEquals(discToUpd.id, ctx.discussionResponse.id)
        assertEquals(discToUpd.title, ctx.discussionResponse.title)
        assertEquals(discToUpd.soundUrl, ctx.discussionResponse.soundUrl)
        assertEquals(discToUpd.status, ctx.discussionResponse.status)
        assertEquals(2, ctx.discussionResponse.answers.size)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
