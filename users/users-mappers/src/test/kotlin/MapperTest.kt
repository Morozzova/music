import musicBroker.api.v1.models.*
import org.junit.Test
import ru.music.common.UsersContext
import ru.music.common.models.*
import ru.music.common.stubs.UsersStubs
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = UsersDiscussionsRequest(
            requestId = "1235",
            debug = UserDebug(
                mode = UserRequestDebugMode.STUB,
                stub = UserRequestDebugStubs.SUCCESS,
            ),
            usersId = "27"
        )

        val context = UsersContext()
        context.fromTransport(req)

        assertEquals(UsersStubs.SUCCESS, context.stubCase)
        assertEquals(UsersWorkMode.STUB, context.workMode)
        assertEquals(UsersUserId("27"), context.usersRequest.id)
    }

    @Test
    fun toTransport() {
        val context = UsersContext(
            requestId = UsersRequestId("1235"),
            command = UsersCommand.USERS_DISCUSSIONS,
            usersResponse = UsersUser(
                login = "login",
                discussions = mutableListOf(
                    UsersDiscussion(
                        id = UsersDiscussionId("777"),
                        title = "Discuuuuuussion",
                        isOpen = false
                    ),
                    UsersDiscussion()
                )
            ),
            errors = mutableListOf(
                UsersError(
                    code = "err0r",
                    group = "request",
                    field = "title",
                    message = "something wrong",
                )
            ),
            state = UsersState.RUNNING,
        )

        val req = context.toTransport() as UsersDiscussionsResponse

        assertEquals("1235", req.requestId)
        assertEquals(2, req.discussions?.size)
        assertEquals("Discuuuuuussion", req.discussions?.get(0)?.title)
        assertEquals(false, req.discussions?.get(0)?.isOpen)
        assertEquals(1, req.errors?.size)
        assertEquals("err0r", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("something wrong", req.errors?.firstOrNull()?.message)
    }
}
