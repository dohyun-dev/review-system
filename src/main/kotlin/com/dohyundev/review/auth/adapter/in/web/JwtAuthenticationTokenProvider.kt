package com.dohyundev.review.auth.adapter.`in`.web

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationTokenProvider(
    private val jwtTokenUtil: JwtTokenUtil,
) {

    fun authenticate(token: String): UsernamePasswordAuthenticationToken? {
        jwtTokenUtil.validateToken(token)

        val employeeId = jwtTokenUtil.getEmployeeId(token)
        val role = jwtTokenUtil.getRole(token)

        return UsernamePasswordAuthenticationToken(
            employeeId,
            null,
            listOf(SimpleGrantedAuthority("ROLE_${role.name}")),
        )
    }
}
