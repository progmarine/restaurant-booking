package almaty.project.booking.config.converter

import almaty.project.booking.domain.BookingStatus
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class BookingStatusConverter : Converter<String, BookingStatus> {

    override fun convert(source: String): BookingStatus {
        return BookingStatus.valueOf(source.uppercase())
    }
}