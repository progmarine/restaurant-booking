package almaty.project.booking.config.converter

import almaty.project.booking.domain.TimeSlotDto
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TimeSlotDtoReadConverter : Converter<LocalDateTime, TimeSlotDto> {

    override fun convert(source: LocalDateTime): TimeSlotDto {
        // Here, we can create a new TimeSlotDto object using the source LocalDateTime value.
        // We can set isAvailable as true since this is only used during reads.
        return TimeSlotDto(timeslotId = 1L, time = source, isAvailable = true, partySize = 1)
    }
}

