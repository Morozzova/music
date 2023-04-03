import musicBroker.api.v1.models.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.music.discussions.api.v1.apiRequestSerialize
import ru.music.discussions.api.v1.apiResponseDeserialize
import ru.music.discussions.app.kafka.AppKafkaConfig
import ru.music.discussions.app.kafka.AppKafkaConsumer
import ru.music.discussions.app.kafka.ConsumerStrategyDisc
import java.util.*
import kotlin.test.assertEquals

class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicIn
        val outputTopic = config.kafkaTopicOut

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyDisc()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiRequestSerialize(
                        DiscussionCreateRequest(
                        requestId = "11111111-1111-1111-1111-111111111111",
                        discussion = DiscussionCreateObject(
                            title = "Very interesting discussion",
                            soundUrl = "http//:url",
                            answers = listOf("the first answer", "the second answer", "the third answer"),
                            status = DiscussionStatus.OPEN
                        ),
                        debug = DiscussionDebug(
                            mode = DiscussionRequestDebugMode.STUB,
                            stub = DiscussionRequestDebugStubs.SUCCESS
                        )
                    )
                    )
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiResponseDeserialize<DiscussionCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("11111111-1111-1111-1111-111111111111", result.requestId)
        assertEquals("Helpmeplz", result.discussion?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}


