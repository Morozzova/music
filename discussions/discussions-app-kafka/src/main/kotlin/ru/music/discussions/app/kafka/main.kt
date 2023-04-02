import ru.music.discussions.app.kafka.AppKafkaConfig
import ru.music.discussions.app.kafka.AppKafkaConsumer
import ru.music.discussions.app.kafka.ConsumerStrategyDisc

fun main() {
    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(config, listOf(ConsumerStrategyDisc()))
    consumer.run()
}