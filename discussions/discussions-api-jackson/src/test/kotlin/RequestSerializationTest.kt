package ru.otus.otuskotlin.marketplace.api.v1

import musicBroker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request = DiscussionCreateRequest(
        requestId = "101",
        debug = DiscussionDebug(
            mode = DiscussionRequestDebugMode.STUB,
            stub = DiscussionRequestDebugStubs.BAD_TITLE
        ),
        discussion = DiscussionCreateObject(
            title = "This is title",
            soundUrl = "/sounds/sound01.wav",
            status = DiscussionStatus.OPEN
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"This is title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as DiscussionCreateRequest

        assertEquals(request, obj)
    }
}
