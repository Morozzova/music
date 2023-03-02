package ru.otus.otuskotlin.marketplace.api.v1

import apiV1Mapper
import musicBroker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = AnswerUpdateResponse(
        requestId = "555",
        answer = AnswerResponseObject(
            discussionId = "678",
            text = "Help me, plz!",
            isRight = true
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"discussionId\":\\s*\"678\""))
        assertContains(json, Regex("\"responseType\":\\s*\"update\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as AnswerUpdateResponse

        assertEquals(response, obj)
    }
}
