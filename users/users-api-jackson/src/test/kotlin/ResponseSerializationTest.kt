package ru.music.users.api.v1

import musicBroker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = AllDiscussionsResponse(
        requestId = "123",
        discussions = listOf(
            DiscussionResponseObject(
                title = "Very interesting discussion"
            )
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"Very interesting discussion\""))
        assertContains(json, Regex("\"responseType\":\\s*\"readAll\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as AllDiscussionsResponse

        assertEquals(response, obj)
    }
}
