package almaty.project.booking.config

import almaty.project.booking.AppProperties
import almaty.project.booking.config.converter.BookingStatusConverter
import almaty.project.booking.config.converter.TimeSlotDtoReadConverter
import almaty.project.booking.config.converter.TimeSlotDtoWriteConverter
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.dialect.PostgresDialect
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
class PostgreConfig(private val appProperties: AppProperties) {

    @Bean
    fun databaseInitializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer =
            ConnectionFactoryInitializer().apply {
                this.setConnectionFactory(connectionFactory)
                if (appProperties.r2dbc.usersEnabled) {
                    this.setDatabasePopulator(CompositeDatabasePopulator().apply {
                        addPopulators(ResourceDatabasePopulator(
                                ClassPathResource("sql/users/users.sql"),
                                ClassPathResource("sql/users/insert.sql")
                        )
                        )
                    }
                    )
                }
                if (appProperties.r2dbc.usersEnabled) {
                    this.setDatabasePopulator(CompositeDatabasePopulator().apply {
                        addPopulators(ResourceDatabasePopulator(
                                ClassPathResource("sql/restaurants/create.sql"),
                                ClassPathResource("sql/restaurants/insert.sql")
                        )
                        )
                    }
                    )
                }
                if (appProperties.r2dbc.reviewsEnabled) {
                    this.setDatabasePopulator(CompositeDatabasePopulator().apply {
                        addPopulators(ResourceDatabasePopulator(
                                ClassPathResource("sql/reviews/create.sql"),
                        )
                        )
                    }
                    )
                }
                if (appProperties.r2dbc.timeSlotsEnabled) {
                    this.setDatabasePopulator(CompositeDatabasePopulator().apply {
                        addPopulators(ResourceDatabasePopulator(
                                ClassPathResource("sql/timeslots/create.sql"),
                        )
                        )
                    }
                    )
                }
                if (appProperties.r2dbc.bookingEnabled) {
                    this.setDatabasePopulator(CompositeDatabasePopulator().apply {
                        addPopulators(ResourceDatabasePopulator(
                                ClassPathResource("sql/booking/create.sql"),
                        )
                        )
                    }
                    )
                }
            }

    @Bean
    fun postgresDialect(
            timeSlotDtoReadConverter: TimeSlotDtoReadConverter,
            timeSlotDtoWriteConverter: TimeSlotDtoWriteConverter,
            bookingStatusConverter: BookingStatusConverter
    ): PostgresDialect {
        return PostgresDialect().apply {
            listOf(timeSlotDtoReadConverter, timeSlotDtoWriteConverter,bookingStatusConverter)
        }
    }
}