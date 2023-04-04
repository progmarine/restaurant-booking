package almaty.project.booking

import almaty.project.booking.repository.UserRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.web.reactive.config.EnableWebFlux

@EnableKafka
@EnableWebFlux
@EnableConfigurationProperties(AppProperties::class)
@ConfigurationPropertiesScan
@SpringBootApplication
@EnableR2dbcRepositories(basePackageClasses = [UserRepository::class])
class BookingApplication

fun main(args: Array<String>) {
    runApplication<BookingApplication>(*args)
}
