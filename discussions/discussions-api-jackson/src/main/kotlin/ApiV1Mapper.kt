package ru.music.discussions.api.v1

import com.fasterxml.jackson.databind.ObjectMapper
import musicBroker.api.v1.models.IRequest
import musicBroker.api.v1.models.IResponse

val apiV1Mapper = ObjectMapper().apply {
//    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//    setSerializationInclusion(JsonInclude.Include.NON_NULL)
}
fun apiRequestSerialize(request: IRequest): String = apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiRequestDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IRequest::class.java) as T

fun apiResponseSerialize(response: IResponse): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiResponseDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IResponse::class.java) as T
