package ru.otus.otuskotlin.marketplace.api.v1

import musicBroker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request = AllDiscussionsRequest(
        requestId = "101",
        debug = UserDebug(
            mode = UserRequestDebugMode.STUB,
            stub = UserRequestDebugStubs.NOT_FOUND
        ),
        allDiscussions = AllDiscussionsReadObject(
            allDiscussions = DiscussionResponseMulti(
                discussions = listOf(
                    DiscussionResponseObject(
                    title = "Very interesting discussion"
                )
                )
            )
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"Very interesting discussion\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"notFound\""))
        assertContains(json, Regex("\"requestType\":\\s*\"readAll\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as AllDiscussionsRequest

        assertEquals(request, obj)
    }
}
