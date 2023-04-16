package ru.music.discussions.biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.music.common.DiscContext
import ru.music.common.models.*
import ru.music.common.stubs.DiscStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DiscussionUpdateStubTest {

    private val processor = DiscussionsProcessor()
    val id = DiscId("888")
    val title = "title 888"
    val soundUrl = "soundurrrrl"
    val ownerId = DiscUserId("0000")
    val answers = mutableListOf(DiscAnswer("Answer number one"), DiscAnswer("Answer number two"))
    val status = DiscStatus.OPEN

    @Test
    fun update() = runTest {

        val ctx = DiscContext(
            command = DiscCommand.UPDATE,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.SUCCESS,
            discussionRequest = DiscDiscussion(
                id = id,
                title = title,
                soundUrl = soundUrl,
                ownerId = ownerId,
                status = status,
                answers = answers
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.discussionResponse.id)
        assertEquals(title, ctx.discussionResponse.title)
        assertEquals(soundUrl, ctx.discussionResponse.soundUrl)
        assertEquals(ownerId, ctx.discussionResponse.ownerId)
        assertEquals(status, ctx.discussionResponse.status)
        assertEquals(answers, ctx.discussionResponse.answers)
    }

    @Test
    fun badId() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.UPDATE,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.BAD_TITLE,
            discussionRequest = DiscDiscussion(
                id = id,
                title = "",
                soundUrl = soundUrl,
                ownerId = ownerId,
                status = status,
                answers = answers
            ),
        )
        processor.exec(ctx)
        assertEquals(DiscDiscussion(), ctx.discussionResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badFile() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.UPDATE,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.BAD_FILE,
            discussionRequest = DiscDiscussion(
                id = id,
                title = title,
                soundUrl = "",
                ownerId = ownerId,
                status = status,
                answers = answers
            ),
        )
        processor.exec(ctx)
        assertEquals(DiscDiscussion(), ctx.discussionResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.UPDATE,
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
            command = DiscCommand.UPDATE,
            state = DiscState.NONE,
            workMode = DiscWorkMode.STUB,
            stubCase = DiscStubs.CANNOT_DELETE,
            discussionRequest = DiscDiscussion(
                id = id,
                title = title,
                soundUrl = soundUrl,
                ownerId = ownerId,
                status = status,
                answers = answers
            ),
        )
        processor.exec(ctx)
        assertEquals(DiscDiscussion(), ctx.discussionResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
