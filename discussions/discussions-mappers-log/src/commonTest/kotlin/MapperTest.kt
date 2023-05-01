import ru.music.common.DiscContext
import ru.music.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromContext() {
        val context = DiscContext(
            requestId = DiscRequestId("1010"),
            command = DiscCommand.CREATE,
            discussionResponse = DiscDiscussion(
                title = "title",
                soundUrl = "www.blablabla.com/very-good-music",
                status = DiscStatus.OPEN
            ),
            errors = mutableListOf(
                DiscError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = DiscState.RUNNING,
        )

        val log = context.toLog("test-id123")

        assertEquals("test-id123", log.logId)
        assertEquals("music-broker", log.source)
        assertEquals("1010", log.discussion?.requestId)
        assertEquals("OPEN", log.discussion?.responseDiscussion?.status)
        val error = log.errors?.firstOrNull()
        assertEquals("wrong title", error?.message)
        assertEquals("ERROR", error?.level)
    }
}
