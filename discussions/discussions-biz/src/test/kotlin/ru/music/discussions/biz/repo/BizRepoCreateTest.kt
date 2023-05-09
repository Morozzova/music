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
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = DiscUserId("321")
    private val command = DiscCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = DiscussionsRepositoryMock(
        invokeCreateDiscussion = {
            DbDiscussionResponse(
                isSuccess = true,
                data = DiscDiscussion(
                    id = DiscId(uuid),
                    title = it.discussion.title,
                    soundUrl = it.discussion.soundUrl,
                    ownerId = userId,
                    status = it.discussion.status
                )
            )
        }
    )
    private val settings = DiscCorSettings(
        repoTest = repo
    )
    private val processor = DiscussionsProcessor(settings)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = DiscContext(
            command = command,
            state = DiscState.NONE,
            workMode = DiscWorkMode.TEST,
            discussionRequest = DiscDiscussion(
                title = "abc",
                soundUrl = "abc",
                status = DiscStatus.OPEN
            ),
        )
        processor.exec(ctx)
        assertEquals(DiscState.FINISHING, ctx.state)
        assertNotEquals(DiscId.NONE, ctx.discussionResponse.id)
        assertEquals("abc", ctx.discussionResponse.title)
        assertEquals("abc", ctx.discussionResponse.soundUrl)
        assertEquals(DiscStatus.OPEN, ctx.discussionResponse.status)
    }
}
