package almaty.project.booking.events.reviewevents

import almaty.project.booking.config.EventConsumerConfig.Companion.GROUP_ID
import almaty.project.booking.config.EventProducerConfig
import almaty.project.booking.events.Event
import almaty.project.booking.service.RestaurantService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ReviewEventsConsumer(private val restaurantService: RestaurantService) {

    @KafkaListener(
            topics = [EventProducerConfig.REVIEW_ADDED_TOPIC],
            containerFactory = "eventListenerContainerFactory",
            groupId = GROUP_ID,
            autoStartup = "\${spring.kafka.events.enable-listener}",
    )
    fun listenReviewAddedEvent(record: ConsumerRecord<String, Event>) {
        try {
            val event = record.value() as ReviewAddedEvent
            restaurantService.addReview(event).subscribe()
            logger.info("Received event [${record.topic()}]: ${record.value()}")
        } catch (e: Exception) {
            logger.error("Could not handle: ${e.message}", e)
        }
    }

    @KafkaListener(
            topics = [EventProducerConfig.REVIEW_EDITED_TOPIC],
            containerFactory = "eventListenerContainerFactory",
            groupId = GROUP_ID,
            autoStartup = "\${spring.kafka.events.enable-listener}",
    )
    fun listenReviewEdited(record: ConsumerRecord<String, Event>) {
        try {
            val event = record.value() as ReviewEditedEvent
            restaurantService.editReview(event).subscribe()
            logger.info("Received event [${record.topic()}]: ${record.value()}")
        } catch (e: Exception) {
            logger.error("Could not handle: ${e.message}", e)
        }
    }

    @KafkaListener(
            topics = [EventProducerConfig.REVIEW_DELETED_TOPIC],
            containerFactory = "eventListenerContainerFactory",
            groupId = GROUP_ID,
            autoStartup = "\${spring.kafka.events.enable-listener}",
    )
    fun listenReviewDeleted(record: ConsumerRecord<String, Event>) {
        try {
            val event = record.value() as ReviewDeletedEvent
            restaurantService.deleteReview(event).subscribe()
            logger.info("Received event [${record.topic()}]: ${record.value()}")
        } catch (e: Exception) {
            logger.error("Could not handle: ${e.message}", e)
        }
    }


    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}