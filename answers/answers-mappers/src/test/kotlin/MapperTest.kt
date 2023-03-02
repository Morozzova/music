import musicBroker.api.v1.models.*
import org.junit.Test
import ru.music.common.AnswersContext
import ru.music.common.models.*
import ru.music.common.stubs.AnswersStubs
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = AnswerCreateRequest(
            requestId = "1234",
            debug = AnswerDebug(
                mode = AnswerRequestDebugMode.STUB,
                stub = AnswerRequestDebugStubs.SUCCESS,
            ),
            answer = AnswerCreateObject(
                discussionId = "444",
                text = "test text",
                isRight = false
            ),
        )

        val context = AnswersContext()
        context.fromTransport(req)

        assertEquals(AnswersStubs.SUCCESS, context.stubCase)
        assertEquals(AnswersWorkMode.STUB, context.workMode)
        assertEquals(AnswersDiscId("444"), context.answersRequest.discId)
        assertEquals(false, context.answersRequest.isRight)
    }

    @Test
    fun toTransport() {
        val context = AnswersContext(
            requestId = AnswersRequestId("1234"),
            command = AnswersCommand.CREATE,
            answersResponse = AnswersAnswer(
                discId = AnswersDiscId("7789"),
                text = "test test test",
                isRight = true
            ),
            errors = mutableListOf(
                AnswersError(
                    code = "errrrrr",
                    group = "request",
                    field = "title",
                    message = "something wrong",
                )
            ),
            state = AnswersState.RUNNING,
        )

        val req = context.toTransport() as AnswerCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("7789", req.answer?.discussionId)
        assertEquals("test test test", req.answer?.text)
        assertEquals(true, req.answer?.isRight)
        assertEquals(1, req.errors?.size)
        assertEquals("errrrrr", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("something wrong", req.errors?.firstOrNull()?.message)
    }
}
