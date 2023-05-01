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
class DiscussionDeleteStubTest {

    private val processor = DiscussionsProcessor()
    val id = DiscId("678")

    @Test
    fun delete() = runTest {

        val ctx = DiscContext(
            command = DiscCommand.DELETE,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.SUCCESS,
            discussionRequest = DiscDiscussion(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = DiscStub.get()
        assertEquals(stub.id, ctx.discussionResponse.id)
        assertEquals(stub.title, ctx.discussionResponse.title)
        assertEquals(stub.soundUrl, ctx.discussionResponse.soundUrl)
        assertEquals(stub.answers, ctx.discussionResponse.answers)
        assertEquals(stub.status, ctx.discussionResponse.status)
        assertEquals(stub.ownerId, ctx.discussionResponse.ownerId)
    }

    @Test
    fun badId() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.DELETE,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.BAD_ID,
            discussionRequest = DiscDiscussion(),
        )
        processor.exec(ctx)
        assertEquals(DiscDiscussion(), ctx.discussionResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.DELETE,
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
            command = DiscCommand.DELETE,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.BAD_TITLE,
            discussionRequest = DiscDiscussion(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(DiscDiscussion(), ctx.discussionResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
