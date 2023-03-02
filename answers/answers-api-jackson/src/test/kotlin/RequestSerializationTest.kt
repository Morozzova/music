package ru.otus.otuskotlin.marketplace.api.v1

import apiV1Mapper
import musicBroker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request = AnswerUpdateRequest(
        requestId = "101",
        debug = AnswerDebug(
            mode = AnswerRequestDebugMode.STUB,
            stub = AnswerRequestDebugStubs.BAD_DISCUSSION
        ),
        answer = AnswerUpdateObject(
            discussionId = "678",
            text = "Help me, plz!",
            isRight = true
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"text\":\\s*\"Help me, plz!\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badDiscussion\""))
        assertContains(json, Regex("\"requestType\":\\s*\"update\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as AnswerUpdateRequest

        assertEquals(request, obj)
    }
}
