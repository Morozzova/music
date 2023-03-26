package ru.music.discussions.api.v1

import musicBroker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = DiscussionCreateResponse(
        requestId = "123",
        discussion = DiscussionResponseObject(
            title = "This is title",
            soundUrl = "/sounds/sound01.wav",
            status = DiscussionStatus.OPEN
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"This is title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as DiscussionCreateResponse

        assertEquals(response, obj)
    }
}
