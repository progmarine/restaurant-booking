package almaty.project.booking.config

import almaty.project.booking.config.EventProducerConfig.Companion.BOOTSTRAP_SERVERS
import almaty.project.booking.events.Event
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer


@Configuration
@EnableKafka
class EventConsumerConfig {

    companion object {
        const val GROUP_ID = "locale"
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Event> {
        val props = mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVERS,
                ConsumerConfig.GROUP_ID_CONFIG to GROUP_ID,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
                JsonDeserializer.TRUSTED_PACKAGES to "*"
        )
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun eventListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Event> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Event>()
        factory.consumerFactory = consumerFactory()
        return factory
    }
}