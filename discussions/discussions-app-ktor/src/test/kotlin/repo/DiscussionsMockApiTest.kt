package repo

import auth.addAuth
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import helpers.testSettings
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import musicBroker.api.v1.models.*
import org.junit.Assert
import org.junit.Test
import ru.music.common.models.*
import ru.music.discussions.module
import ru.music.discussions.repo.tests.DiscussionsRepositoryMock
import ru.music.discussions.ru.music.discussions.base.KtorAuthConfig
import ru.music.discussions.stubs.DiscStub
import kotlin.test.assertEquals

class DiscussionsMockApiTest {
    private val stub = DiscStub.get()
    private val userId = stub.ownerId
    private val discId = stub.id

    @Test
    fun create() = testApplication {
        val repo = DiscussionsRepositoryMock(
            invokeCreateDiscussion = {
                DbDiscussionResponse(
                    isSuccess = true,
                    data = it.discussion.copy(id = discId),
                )
            }
        )
        application {
            module(testSettings(repo))
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
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(discId.asString(), responseObj.discussion?.id)
        assertEquals(createDiscussion.title, responseObj.discussion?.title)
        assertEquals(createDiscussion.soundUrl, responseObj.discussion?.soundUrl)
        assertEquals(createDiscussion.status, responseObj.discussion?.status)
        assertEquals(createDiscussion.answers, responseObj.discussion?.answers)

    }

    @Test
    fun read() = testApplication {
        val repo = DiscussionsRepositoryMock(
            invokeReadDiscussion = {
                DbDiscussionResponse(
                    isSuccess = true,
                    data = DiscDiscussion(
                        id = it.id,
                        ownerId = userId,
                    ),
                )
            }
        )
        application {
            module(testSettings(repo))
        }
        val client = myClient()

        val response = client.post("/discussion/read") {
            val requestObj = DiscussionReadRequest(
                requestId = "12345",
                discussion = DiscussionReadObject(discId.asString()),
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
            setBody(requestObj)
        }
        val responseObj = response.body<DiscussionReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(discId.asString(), responseObj.discussion?.id)
    }
    @Test
    fun update() = testApplication {
        val repo = DiscussionsRepositoryMock(
            invokeReadDiscussion = {
                DbDiscussionResponse(
                    isSuccess = true,
                    data = DiscDiscussion(
                        id = it.id,
                        ownerId = userId,
                        lock = DiscLock("123")
                    ),
                )
            },
            invokeUpdateDiscussion = {
                DbDiscussionResponse(
                    isSuccess = true,
                    data = it.discussion.copy(ownerId = userId),
                )
            }
        )
        application {
            module(testSettings(repo))
        }
        val client = myClient()

        val discussionUpdate = DiscussionUpdateObject(
            id = "555",
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
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
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
        val repo = DiscussionsRepositoryMock(
            invokeReadDiscussion = {
                DbDiscussionResponse(
                    isSuccess = true,
                    data = DiscDiscussion(
                        id = it.id,
                        ownerId = userId,
                    ),
                )
            },
            invokeCloseDiscussion = {
                DbDiscussionResponse(
                    isSuccess = true,
                    data = it.discussion.copy(ownerId = userId, status = DiscStatus.CLOSED),
                )
            }
        )
        application {
            module(testSettings(repo))
        }
        val client = myClient()

        val discussionClose = DiscussionCloseObject(
            id = "555",
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
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
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
        application {
            val repo = DiscussionsRepositoryMock(
                invokeReadDiscussion = {
                    DbDiscussionResponse(
                        isSuccess = true,
                        data = DiscDiscussion(
                            id = it.id,
                            ownerId = userId,
                        ),
                    )
                },
                invokeDeleteDiscussion = {
                    DbDiscussionResponse(
                        isSuccess = true,
                        data = DiscDiscussion(
                            id = it.id,
                            ownerId = userId,
                        )
                    )
                }
            )
            module(testSettings(repo))
        }

        val client = myClient()

        val response = client.post("/discussion/delete") {
            val requestObj = DiscussionDeleteRequest(
                requestId = "12345",
                discussion = DiscussionDeleteObject(
                    id = "248",
                ),
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
            setBody(requestObj)
        }

        val responseObj = response.body<DiscussionDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("248", responseObj.discussion?.id)
    }

    @Test
    fun allDiscussions() = testApplication {

        application {
            val repo =
                DiscussionsRepositoryMock(
                    invokeAllDiscussions = {
                        DbDiscussionsResponse(
                            isSuccess = true,
                            data = listOf(
                                DiscDiscussion(
                                    id = DiscId("456")
                                ),
                                DiscDiscussion(
                                    id = DiscId("567")
                                )
                            ),
                        )
                    }
                )
            module(testSettings(repo))
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
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
            setBody(requestObj)
        }
        val responseObj = response.body<AllDiscussionsResponse>()
        assertEquals(200, response.status.value)
        assertEquals(2, responseObj.discussions?.size)
        Assert.assertNotEquals(0, responseObj.discussions?.size)
        assertEquals("456", responseObj.discussions?.first()?.id)
    }

    @Test
    fun usersDiscussions() = testApplication {
        application {
            val repo =
                DiscussionsRepositoryMock(
                    invokeUsersDiscussions = {
                        DbDiscussionsResponse(
                            isSuccess = true,
                            data = listOf(
                                DiscDiscussion(
                                    ownerId = it.usersId!!
                                ),
                                DiscDiscussion(
                                    ownerId = DiscUserId("567")
                                )
                            ),
                        )
                    }
                )
            module(testSettings(repo))
        }
        val client = myClient()

        val response = client.post("/discussion/users") {
            val requestObj = UsersDiscussionsRequest(
                requestId = "12345",
                debug = DiscussionDebug(
                    mode = DiscussionRequestDebugMode.TEST,
                ),
                usersId = "567"
            )
            contentType(ContentType.Application.Json)
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
            setBody(requestObj)
        }
        val responseObj = response.body<UsersDiscussionsResponse>()
        assertEquals(200, response.status.value)
        Assert.assertNotEquals(0, responseObj.discussions?.size)
        assertEquals("567", responseObj.discussions?.first()?.ownerId)
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
