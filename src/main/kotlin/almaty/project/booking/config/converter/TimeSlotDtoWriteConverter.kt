package almaty.project.booking.config.converter

import almaty.project.booking.domain.TimeSlotDto
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TimeSlotDtoWriteConverter : Converter<TimeSlotDto, LocalDateTime> {

    override fun convert(source: TimeSlotDto): LocalDateTime {
        // Here, we can extract the LocalDateTime value from the TimeSlotDto object.
        return source.time ?: LocalDateTime.now() // Use current time if time value is null
    }

}