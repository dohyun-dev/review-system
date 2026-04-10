package com.dohyundev.review.auth.application.port.`in`

import com.dohyundev.review.auth.application.port.`in`.command.LoginCommand
import com.dohyundev.review.auth.domain.TokenSet

interface LoginUseCase {
    fun login(command: LoginCommand): TokenSet
}