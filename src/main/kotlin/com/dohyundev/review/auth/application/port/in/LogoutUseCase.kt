package com.dohyundev.review.auth.application.port.`in`

interface LogoutUseCase {
    fun logout(refreshToken: String)
}