package com.dohyundev.review.auth.adapter.`in`.web

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class BearerTokenResolver : TokenResolver {

    override fun resolve(request: HttpServletRequest): String? {
        val bearer = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return if (bearer.startsWith(BEARER_PREFIX)) bearer.substring(BEARER_PREFIX.length) else null
    }

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }
}
