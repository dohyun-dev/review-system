package com.dohyundev.review.auth.adapter.`in`.web

import jakarta.servlet.http.HttpServletRequest

interface TokenResolver {
    fun resolve(request: HttpServletRequest): String?
}
