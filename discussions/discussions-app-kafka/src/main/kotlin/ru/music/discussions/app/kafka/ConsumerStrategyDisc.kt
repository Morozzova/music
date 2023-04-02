package ru.music.discussions.app.kafka

import fromTransport
import musicBroker.api.v1.models.IRequest
import musicBroker.api.v1.models.IResponse
import ru.music.common.DiscContext
import ru.music.discussions.api.v1.apiRequestDeserialize
import ru.music.discussions.api.v1.apiResponseSerialize
import toTransport

class ConsumerStrategyDisc : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicIn, config.kafkaTopicOut)
    }

    override fun serialize(source: DiscContext): String {
        val response: IResponse = source.toTransport()
        return apiResponseSerialize(response)
    }

    override fun deserialize(value: String, target: DiscContext) {
        val request: IRequest = apiRequestDeserialize(value)
        target.fromTransport(request)
    }
}