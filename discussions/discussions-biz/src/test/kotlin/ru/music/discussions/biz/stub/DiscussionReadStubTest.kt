package ru.music.discussions.biz.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.music.common.DiscContext
import ru.music.common.models.*
import ru.music.common.stubs.DiscStubs
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.stubs.DiscStub
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DiscussionReadStubTest {

    private val processor = DiscussionsProcessor()
    val id = DiscId("678")

    @Test
    fun read() = runTest {

        val ctx = DiscContext(
            command = DiscCommand.READ,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.SUCCESS,
            discussionRequest = DiscDiscussion(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (DiscStub.get()) {
            assertEquals(id, ctx.discussionResponse.id)
            assertEquals(title, ctx.discussionResponse.title)
            assertEquals(soundUrl, ctx.discussionResponse.soundUrl)
            assertEquals(answers, ctx.discussionResponse.answers)
            assertEquals(status, ctx.discussionResponse.status)
            assertEquals(ownerId, ctx.discussionResponse.ownerId)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.READ,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.BAD_ID,
            discussionRequest = DiscDiscussion(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(DiscDiscussion(), ctx.discussionResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.READ,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.DB_ERROR,
            discussionRequest = DiscDiscussion(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(DiscDiscussion(), ctx.discussionResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.READ,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.NOT_FOUND,
            discussionRequest = DiscDiscussion(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(DiscDiscussion(), ctx.discussionResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
