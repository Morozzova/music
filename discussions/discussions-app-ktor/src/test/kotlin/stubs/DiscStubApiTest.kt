package ru.music.discussions.stubs

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import musicBroker.api.v1.models.*
import org.junit.Test
import ru.music.discussions.moduleJvm
import kotlin.test.assertEquals

class DiscStubApiTest {
    @Test
    fun create() = testApplication {
        application {
            moduleJvm()
        }
        val client = myClient()

        val response = client.post("/discussions/disc/create") {
            val requestObj = DiscussionCreateRequest (
                requestId = "12345",
                discussion = DiscussionCreateObject(
                    title = "Ляляля",
                    soundUrl = "www.lalala.ru",
                    status = DiscussionStatus.OPEN
                ),
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.STUB,
                    stub = DiscussionRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionCreateResponse>()
        println(responseObj)
        assertEquals(200, response.status.value)
        assertEquals("678", responseObj.discussion?.id)
    }

    @Test
    fun read() = testApplication {
        application {
            moduleJvm()
        }
        val client = myClient()

        val response = client.post("/discussions/disc/read") {
            val requestObj = DiscussionReadRequest(
                requestId = "12345",
                discussion = DiscussionReadObject(
                    id = "678"
                ),
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.STUB,
                    stub = DiscussionRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("678", responseObj.discussion?.id)
    }

    @Test
    fun update() = testApplication {
        application {
            moduleJvm()
        }
        val client = myClient()

        val response = client.post("/discussions/disc/update") {
            val requestObj = DiscussionUpdateRequest(
                requestId = "12345",
                discussion = DiscussionUpdateObject(
                    title = "Ляляля",
                    soundUrl = "www.bebebe.ru",
                    status = DiscussionStatus.OPEN
                ),
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.STUB,
                    stub = DiscussionRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("678", responseObj.discussion?.id)
    }

    @Test
    fun close() = testApplication {
        application {
            moduleJvm()
        }
        val client = myClient()

        val response = client.post("/discussions/disc/close") {
            val requestObj = DiscussionCloseRequest(
                requestId = "12345",
                discussion = DiscussionCloseObject(
                    title = "Ляляля",
                    soundUrl = "www.lalala.ru",
                    status = DiscussionStatus.CLOSED
                ),
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.STUB,
                    stub = DiscussionRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionCloseResponse>()
        assertEquals(200, response.status.value)
        assertEquals("678", responseObj.discussion?.id)
        assertEquals(DiscussionStatus.OPEN, responseObj.discussion?.status)
    }

    @Test
    fun delete() = testApplication {
        application {
            moduleJvm()
        }
        val client = myClient()

        val response = client.post("/discussions/disc/delete") {
            val requestObj = DiscussionDeleteRequest(
                requestId = "12345",
                discussion = DiscussionDeleteObject(
                    id = "678"
                ),
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.STUB,
                    stub = DiscussionRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("678", responseObj.discussion?.id)
    }

    @Test
    fun allDiscussions() = testApplication {
        application {
            moduleJvm()
        }
        val client = myClient()

        val response = client.post("/discussions/disc/all") {
            val requestObj = AllDiscussionsRequest(
                requestId = "12345",
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.STUB,
                    stub = DiscussionRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AllDiscussionsResponse>()
        assertEquals(200, response.status.value)
        assertEquals(3, responseObj.discussions?.size)
    }

    @Test
    fun usersDiscussions() = testApplication {
        application {
            moduleJvm()
        }
        val client = myClient()

        val response = client.post("/discussions/disc/users") {
            val requestObj = UsersDiscussionsRequest(
                requestId = "12345",
                usersId = "888",
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.STUB,
                    stub = DiscussionRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<UsersDiscussionsResponse>()
        assertEquals(200, response.status.value)
        assertEquals(2, responseObj.discussions?.size)
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
