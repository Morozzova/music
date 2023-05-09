package repo

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import musicBroker.api.v1.models.*
import org.junit.Assert.assertNotEquals
import org.junit.Test
import ru.music.common.DiscCorSettings
import ru.music.common.models.DiscAnswer
import ru.music.common.models.DiscId
import ru.music.common.models.DiscStatus
import ru.music.common.models.DiscUserId
import ru.music.discussions.DiscAppSettings
import ru.music.discussions.module
import ru.music.discussions.repo.inmemory.DiscussionsRepoInMemory
import ru.music.discussions.stubs.DiscStub
import kotlin.test.assertEquals

class DiscussionsInMemoryApiTest {
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val uuidSup = "10000000-0000-0000-0000-000000000003"
    private val usersId = "777"
    private val initDiscussion = DiscStub.prepareResult {
        id = DiscId(uuidOld)
        title = "abc"
        soundUrl = "abc"
        status = DiscStatus.CLOSED
        answers = mutableListOf(DiscAnswer("333"), DiscAnswer("444"))
    }
    private val initDiscussionSupply = DiscStub.prepareResult {
        id = DiscId(uuidSup)
        title = "abc"
        soundUrl = "abc"
        status = DiscStatus.OPEN
        answers = mutableListOf()
        ownerId = DiscUserId(usersId)
    }

    @Test
    fun create() = testApplication {
        val repo = DiscussionsRepoInMemory(initObjects = listOf(initDiscussion), randomUuid = { uuidNew })
        application {
            module(DiscAppSettings(corSettings = DiscCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val createDiscussion = DiscussionCreateObject(
            title = "Music song",
            soundUrl = "abc",
            status = DiscussionStatus.OPEN
        )

        val response = client.post("/discussion/create") {
            val requestObj = DiscussionCreateRequest(
                requestId = "12345",
                discussion = createDiscussion,
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidNew, responseObj.discussion?.id)
        assertEquals(createDiscussion.title, responseObj.discussion?.title)
        assertEquals(createDiscussion.soundUrl, responseObj.discussion?.soundUrl)
        assertEquals(createDiscussion.status, responseObj.discussion?.status)
        assertEquals(createDiscussion.answers, responseObj.discussion?.answers)
    }

    @Test
    fun read() = testApplication {
        val repo = DiscussionsRepoInMemory(initObjects = listOf(initDiscussion), randomUuid = { uuidNew })
        application {
            module(DiscAppSettings(corSettings = DiscCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/discussion/read") {
            val requestObj = DiscussionReadRequest(
                requestId = "12345",
                discussion = DiscussionReadObject(uuidOld),
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.discussion?.id)
    }

    @Test
    fun update() = testApplication {
        val repo = DiscussionsRepoInMemory(initObjects = listOf(initDiscussion), randomUuid = { uuidNew })
        application {
            module(DiscAppSettings(corSettings = DiscCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val discussionUpdate = DiscussionUpdateObject(
            id = uuidOld,
            title = "Very good music",
            soundUrl = "abc.ru",
            status = DiscussionStatus.OPEN,
            answers = mutableListOf("111", "222", "000")
        )

        val response = client.post("/discussion/update") {
            val requestObj = DiscussionUpdateRequest(
                requestId = "12345",
                discussion = discussionUpdate,
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(discussionUpdate.id, responseObj.discussion?.id)
        assertEquals(discussionUpdate.title, responseObj.discussion?.title)
        assertEquals(discussionUpdate.soundUrl, responseObj.discussion?.soundUrl)
        assertEquals(discussionUpdate.status, responseObj.discussion?.status)
        assertEquals(discussionUpdate.answers, responseObj.discussion?.answers)
    }

    @Test
    fun close() = testApplication {
        val repo = DiscussionsRepoInMemory(initObjects = listOf(initDiscussion), randomUuid = { uuidNew })
        application {
            module(DiscAppSettings(corSettings = DiscCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val discussionClose = DiscussionCloseObject(
            id = uuidOld,
            title = "Very good music",
            soundUrl = "abc.ru",
            status = DiscussionStatus.CLOSED,
            answers = mutableListOf("111", "222", "000")
        )

        val response = client.post("/discussion/close") {
            val requestObj = DiscussionCloseRequest(
                requestId = "12345",
                discussion = discussionClose,
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionCloseResponse>()
        assertEquals(200, response.status.value)
        assertEquals(discussionClose.id, responseObj.discussion?.id)
        assertEquals(discussionClose.title, responseObj.discussion?.title)
        assertEquals(discussionClose.soundUrl, responseObj.discussion?.soundUrl)
        assertEquals(discussionClose.status, responseObj.discussion?.status)
        assertEquals(discussionClose.answers, responseObj.discussion?.answers)
    }

    @Test
    fun delete() = testApplication {
        val repo = DiscussionsRepoInMemory(initObjects = listOf(initDiscussion), randomUuid = { uuidNew })
        application {
            module(DiscAppSettings(corSettings = DiscCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/discussion/delete") {
            val requestObj = DiscussionDeleteRequest(
                requestId = "12345",
                discussion = DiscussionDeleteObject(
                    id = uuidOld,
                ),
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.discussion?.id)
    }

    @Test
    fun allDiscussions() = testApplication {
        val repo = DiscussionsRepoInMemory(initObjects = listOf(initDiscussion, initDiscussionSupply), randomUuid = { uuidNew })
        application {
            module(DiscAppSettings(corSettings = DiscCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/discussion/all") {
            val requestObj = AllDiscussionsRequest(
                requestId = "12345",
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AllDiscussionsResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.discussions?.size)
        println("************************************************************************************************************")
        println(responseObj)
        assertEquals(uuidOld, responseObj.discussions?.first()?.id)
    }

    @Test
    fun usersDiscussions() = testApplication {
        val repo = DiscussionsRepoInMemory(initObjects = listOf(initDiscussion, initDiscussionSupply), randomUuid = { uuidNew })
        application {
            module(DiscAppSettings(corSettings = DiscCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/discussion/users") {
            val requestObj = UsersDiscussionsRequest(
                requestId = "12345",
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.TEST,
                ),
                usersId = usersId
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<UsersDiscussionsResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.discussions?.size)
        assertEquals(usersId, responseObj.discussions?.first()?.ownerId)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }
}
