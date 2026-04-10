package com.dohyundev.review.auth.adapter.`in`.web

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val tokenResolvers: List<TokenResolver>,
    private val jwtAuthenticationTokenProvider: JwtAuthenticationTokenProvider,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = tokenResolvers.firstNotNullOfOrNull { it.resolve(request) }

        if (token != null) {
            try {
                val jwtAuthorizedAuthenticationToken = jwtAuthenticationTokenProvider.authenticate(token)
                SecurityContextHolder.getContext().authentication = jwtAuthorizedAuthenticationToken
            } catch (exception: Exception) {
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }
}
