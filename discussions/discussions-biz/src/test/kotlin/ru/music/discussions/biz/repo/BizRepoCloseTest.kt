package ru.music.discussions.biz.repo

import DiscussionsRepositoryMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import repo.DbDiscussionResponse
import ru.music.common.DiscContext
import ru.music.common.DiscCorSettings
import ru.music.common.models.*
import ru.music.discussions.biz.DiscussionsProcessor
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoCloseTest {
    private val userId = DiscUserId("321")
    private val command = DiscCommand.CLOSE

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
            invokeCloseDiscussion = {
                DbDiscussionResponse(
                    isSuccess = true,
                    data = DiscDiscussion(
                        id = initDisc.id,
                        title = initDisc.title,
                        soundUrl = initDisc.soundUrl,
                        status = DiscStatus.CLOSED,
                        answers = initDisc.answers
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
    fun repoCloseSuccessTest() = runTest {
        val discToUpd = DiscDiscussion(
            id = initDisc.id,
            title = initDisc.title,
            soundUrl = initDisc.soundUrl,
            status = DiscStatus.CLOSED,
            answers = initDisc.answers,
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
        assertEquals(1, ctx.discussionResponse.answers.size)
    }

    @Test
    fun repoCloseNotFoundTest() = repoNotFoundTest(command)
}
