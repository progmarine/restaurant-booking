package almaty.project.booking

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "app")
data class AppProperties(
        val r2dbc: R2dbc
) {
    data class R2dbc(
            val usersEnabled: Boolean,
            val restaurantsEnabled: Boolean,
            val reviewsEnabled: Boolean,
            val timeSlotsEnabled: Boolean,
            val bookingEnabled: Boolean
    )
}
