package com.dohyundev.review.auth.adapter.`in`.web.api

import com.dohyundev.review.auth.adapter.`in`.web.api.request.LoginRequest
import com.dohyundev.review.auth.adapter.`in`.web.api.response.TokenSetResponse
import com.dohyundev.review.auth.application.port.`in`.command.LoginCommand
import com.dohyundev.review.auth.domain.TokenSet
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface AuthCommandMapper {
    fun toCommand(request: LoginRequest): LoginCommand
    fun toResponse(tokenSet: TokenSet): TokenSetResponse
}
