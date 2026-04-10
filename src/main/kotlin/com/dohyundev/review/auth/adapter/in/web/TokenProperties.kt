package com.dohyundev.review.auth.adapter.`in`.web

import io.jsonwebtoken.security.Keys
import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.crypto.SecretKey
import java.time.Duration

@Component
@Validated
@ConfigurationProperties(prefix = "auth.token")
class TokenProperties {
    @field:NotBlank
    var secret: String = ""

    var accessTokenExpiration: Duration = Duration.ofHours(1)
    var refreshTokenExpiration: Duration = Duration.ofDays(14)

    val secretKey: SecretKey by lazy { Keys.hmacShaKeyFor(secret.toByteArray()) }
}
