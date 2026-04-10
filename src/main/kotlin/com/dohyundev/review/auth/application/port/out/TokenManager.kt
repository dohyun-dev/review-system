package com.dohyundev.review.auth.application.port.out

import com.dohyundev.review.auth.domain.RefreshToken
import com.dohyundev.review.auth.domain.TokenSet
import com.dohyundev.review.employee.command.domain.Employee

interface TokenManager {
    fun generateToken(employee: Employee): TokenSet
    fun validateToken(token: String)
    fun parseRefreshToken(token: String): RefreshToken
}