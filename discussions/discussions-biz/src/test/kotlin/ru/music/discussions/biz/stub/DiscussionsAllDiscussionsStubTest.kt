package ru.music.discussions.biz.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.music.common.DiscContext
import ru.music.common.models.*
import ru.music.common.stubs.DiscStubs
import ru.music.discussions.biz.DiscussionsProcessor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class DiscussionsAllDiscussionsStubTest {

    private val processor = DiscussionsProcessor()
    val discussions = DiscMulti()

    @Test
    fun allDiscussions() = runTest {

        val ctx = DiscContext(
            command = DiscCommand.ALL_DISCUSSIONS,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.SUCCESS,
            multiDiscussionsRequest = discussions,
        )
        processor.exec(ctx)

        assertTrue(ctx.multiDiscussionsResponse.size > 1)
        val first = ctx.multiDiscussionsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(ctx.discussionResponse.title))
        assertTrue(first.soundUrl.contains(ctx.discussionResponse.soundUrl))
        assertEquals(first.answers, ctx.discussionResponse.answers)
        assertEquals(first.status, ctx.discussionResponse.status)
        assertEquals(first.ownerId, ctx.discussionResponse.ownerId)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.ALL_DISCUSSIONS,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.DB_ERROR,
            multiDiscussionsRequest = discussions,
        )
        processor.exec(ctx)
        assertEquals(mutableListOf(), ctx.multiDiscussionsResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.ALL_DISCUSSIONS,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.CANNOT_DELETE,
            multiDiscussionsRequest = discussions,
        )
        processor.exec(ctx)
        assertEquals(mutableListOf(), ctx.multiDiscussionsResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
