package com.dohyundev.review.auth.adapter.`in`.web

import com.dohyundev.review.auth.application.port.out.TokenManager
import com.dohyundev.review.auth.domain.RefreshToken
import com.dohyundev.review.auth.domain.TokenSet
import com.dohyundev.review.auth.domain.exception.ExpiredTokenException
import com.dohyundev.review.auth.domain.exception.InvalidTokenException
import com.dohyundev.review.employee.command.domain.Employee
import com.dohyundev.review.employee.command.domain.EmployeeRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.UUID

@Component
class JwtTokenUtil(
    private val tokenProperties: TokenProperties,
) : TokenManager {

    override fun generateToken(employee: Employee): TokenSet {
        val issuedAt = LocalDateTime.now()
        val accessTokenExpiresAt = issuedAt.plus(tokenProperties.accessTokenExpiration)
        val refreshTokenExpiresAt = issuedAt.plus(tokenProperties.refreshTokenExpiration)

        return TokenSet(
            accessToken = generateAccessToken(employee, issuedAt, accessTokenExpiresAt),
            refreshToken = generateRefreshToken(employee, issuedAt, refreshTokenExpiresAt),
            accessTokenExpiresAt = accessTokenExpiresAt,
            refreshTokenExpiresAt = refreshTokenExpiresAt,
        )
    }

    fun generateAccessToken(
        employee: Employee,
        issuedAt: LocalDateTime = LocalDateTime.now(),
        expiresAt: LocalDateTime = issuedAt.plus(tokenProperties.accessTokenExpiration),
    ): String {
        return Jwts.builder()
            .subject(employee.id.toString())
            .issuedAt(issuedAt.toDate())
            .claim("role", employee.role.name)
            .expiration(expiresAt.toDate())
            .signWith(tokenProperties.secretKey)
            .compact()
    }

    fun generateRefreshToken(
        employee: Employee,
        issuedAt: LocalDateTime = LocalDateTime.now(),
        expiresAt: LocalDateTime = issuedAt.plus(tokenProperties.refreshTokenExpiration),
    ): String {
        return Jwts.builder()
            .subject(employee.id.toString())
            .issuedAt(issuedAt.toDate())
            .expiration(expiresAt.toDate())
            .signWith(tokenProperties.secretKey)
            .id(UUID.randomUUID().toString())
            .compact()
    }

    override fun validateToken(token: String) {
        parseClaims(token)
    }

    override fun parseRefreshToken(token: String): RefreshToken {
        val claims = parseClaims(token)
        return RefreshToken(
            token = token,
            employeeId = claims.subject.toLong(),
            expiresAt = claims.expiration.toLocalDateTime(),
        )
    }

    fun getEmployeeId(token: String): Long {
        return parseClaims(token).subject.toLong()
    }

    fun getRole(token: String): EmployeeRole {
        val claims = parseClaims(token)
        val roleName = claims.get("role", String::class.java)
        return EmployeeRole.valueOf(roleName)
    }

    private fun parseClaims(token: String): Claims {
        try {
            return Jwts.parser()
                .verifyWith(tokenProperties.secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (ex: ExpiredJwtException) {
            throw ExpiredTokenException(cause = ex)
        } catch (ex: Exception) {
            throw InvalidTokenException(cause = ex)
        }
    }

    private fun LocalDateTime.toDate(): Date =
        Date.from(this.atZone(ZoneId.systemDefault()).toInstant())

    private fun Date.toLocalDateTime(): LocalDateTime =
        this.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}
