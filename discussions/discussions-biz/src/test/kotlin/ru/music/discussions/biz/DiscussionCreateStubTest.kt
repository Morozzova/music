package ru.music.discussions.biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.music.common.DiscContext
import ru.music.common.models.*
import ru.music.common.stubs.DiscStubs
import ru.music.discussions.stubs.DiscStub
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DiscussionCreateStubTest {

    private val processor = DiscussionsProcessor()
    val id = DiscId("666")
    val title = "title 666"
    val soundUrl = "soundurrrl"
    val ownerId = DiscUserId("09876")
    val answers = mutableListOf(DiscAnswer("Ответ номер раз"), DiscAnswer("Ответ номер два"))
    val status = DiscStatus.CLOSED

    @Test
    fun create() = runTest {

        val ctx = DiscContext(
            command = DiscCommand.CREATE,
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
        assertEquals(DiscStub.get().id, ctx.discussionResponse.id)
        assertEquals(title, ctx.discussionResponse.title)
        assertEquals(soundUrl, ctx.discussionResponse.soundUrl)
        assertEquals(ownerId, ctx.discussionResponse.ownerId)
        assertEquals(status, ctx.discussionResponse.status)
        assertEquals(answers, ctx.discussionResponse.answers)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = DiscContext(
            command = DiscCommand.CREATE,
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
            command = DiscCommand.CREATE,
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
            command = DiscCommand.CREATE,
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
            command = DiscCommand.CREATE,
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
