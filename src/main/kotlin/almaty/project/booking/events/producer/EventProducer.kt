package almaty.project.booking.events.producer

import almaty.project.booking.events.Event
import reactor.core.publisher.Mono

interface EventProducer {

    fun <T : Event> sendEvent(event: T): Mono<T>

}