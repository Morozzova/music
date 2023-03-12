import musicBroker.api.v1.models.*
import org.junit.Test
import ru.music.common.DiscContext
import ru.music.common.models.*
import ru.music.common.stubs.DiscStubs
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = DiscussionCreateRequest(
            requestId = "1234",
            debug = DiscussionDebug(
                mode = DiscussionRequestDebugMode.STUB,
                stub = DiscussionRequestDebugStubs.SUCCESS,
            ),
            discussion = DiscussionCreateObject(
                title = "title",
                soundUrl = "soundUrl",
                status = DiscussionStatus.CLOSED
            ),
        )

        val context = DiscContext()
        context.fromTransport(req)

        assertEquals(DiscStubs.SUCCESS, context.stubCase)
        assertEquals(DiscWorkMode.STUB, context.workMode)
        assertEquals("title", context.discussionRequest.title)
        assertEquals(DiscStatus.CLOSED, context.discussionRequest.status)
    }

    @Test
    fun toTransport() {
        val context = DiscContext(
            requestId = DiscRequestId("1234"),
            command = DiscCommand.CREATE,
            discussionResponse = DiscDiscussion(
                title = "title",
                soundUrl = "sound",
                status = DiscStatus.OPEN
            ),
            errors = mutableListOf(
                DiscError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "something wrong",
                )
            ),
            state = DiscState.RUNNING,
        )

        val req = context.toTransport() as DiscussionCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("title", req.discussion?.title)
        assertEquals("sound", req.discussion?.soundUrl)
        assertEquals(DiscussionStatus.OPEN, req.discussion?.status)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("something wrong", req.errors?.firstOrNull()?.message)
    }
}
