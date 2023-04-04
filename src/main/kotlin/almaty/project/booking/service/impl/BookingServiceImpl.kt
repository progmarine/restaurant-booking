package almaty.project.booking.service.impl

import almaty.project.booking.commands.CreateBookingCommand
import almaty.project.booking.commands.UpdateBookingCommand
import almaty.project.booking.domain.Booking
import almaty.project.booking.domain.BookingStatus
import almaty.project.booking.events.bookingevents.BookingCreatedEvent
import almaty.project.booking.events.bookingevents.BookingUpdatedEvent
import almaty.project.booking.events.producer.EventProducer
import almaty.project.booking.repository.BookingRepository
import almaty.project.booking.service.BookingService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BookingServiceImpl(private val bookingRepository: BookingRepository, private val eventProducer: EventProducer) : BookingService {

    override fun createBooking(command: CreateBookingCommand): Mono<BookingCreatedEvent> {
        return command.toBookingEntity(status = BookingStatus.NEW)
                .flatMap { bookingRepository.save(it) }
                .flatMap { booking ->
                    val event = BookingCreatedEvent(
                            bookingId = booking.id!!,
                            userId = booking.userId,
                            restaurantId = booking.restaurantId,
                            timeSlotId = booking.timeSlotId,
                            partySize = booking.partySize,
                            specialRequest = booking.specialRequest
                    )
                    eventProducer.sendEvent(event)
                }
    }

    override fun updateBooking(command: UpdateBookingCommand): Mono<Booking> { // ||
        return bookingRepository.findById(command.bookingId)
                .flatMap { booking ->
                    command.timeSlotId?.let { booking.timeSlotId = it }
                    command.status?.let { booking.status = it.toString() }
                    command.partySize?.let { booking.partySize = it }
                    command.specialRequests?.let { booking.specialRequest = it }
                    bookingRepository.save(booking)
                }
                .doOnSuccess { booking ->
                    val event = BookingUpdatedEvent(
                            bookingId = booking.id!!,
                            userId = booking.userId,
                            restaurantId = booking.restaurantId,
                            timeSlotId = booking.timeSlotId,
                            partySize = booking.partySize,
                            specialRequest = booking.specialRequest
                    )
                    eventProducer.sendEvent(event)
                }
    }
}