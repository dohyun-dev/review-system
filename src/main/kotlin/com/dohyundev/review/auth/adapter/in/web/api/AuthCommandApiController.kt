package com.dohyundev.review.auth.adapter.`in`.web.api

import com.dohyundev.review.auth.adapter.`in`.web.api.request.LoginRequest
import com.dohyundev.review.auth.adapter.`in`.web.api.request.LogoutRequest
import com.dohyundev.review.auth.adapter.`in`.web.api.request.TokenRefreshRequest
import com.dohyundev.review.auth.adapter.`in`.web.api.response.TokenSetResponse
import com.dohyundev.review.auth.application.port.`in`.LoginUseCase
import com.dohyundev.review.auth.application.port.`in`.LogoutUseCase
import com.dohyundev.review.auth.application.port.`in`.TokenRefreshUseCase
import com.dohyundev.review.auth.domain.exception.InvalidTokenException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "인증", description = "로그인, 토큰 갱신, 로그아웃 API")
@RestController
@RequestMapping("/api/auth")
class AuthCommandApiController(
    private val loginUseCase: LoginUseCase,
    private val tokenRefreshUseCase: TokenRefreshUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val authCommandMapper: AuthCommandMapper,
) {

    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<TokenSetResponse> {
        val tokenSet = loginUseCase.login(authCommandMapper.toCommand(request))
        return ResponseEntity.ok(authCommandMapper.toResponse(tokenSet))
    }

    @Operation(summary = "토큰 갱신")
    @ApiResponse(responseCode = "200", description = "갱신 성공")
    @PostMapping("/token/refresh")
    fun refresh(@RequestBody @Valid request: TokenRefreshRequest): ResponseEntity<TokenSetResponse> {
        if (request.refreshToken.isNullOrBlank())
            throw InvalidTokenException()

        val tokenSet = tokenRefreshUseCase.refresh(request.refreshToken)

        return ResponseEntity.ok(authCommandMapper.toResponse(tokenSet))
    }

    @Operation(summary = "로그아웃")
    @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    @PostMapping("/logout")
    fun logout(@RequestBody request: LogoutRequest): ResponseEntity<Void> {
        if (request.refreshToken.isNullOrBlank())
            return ResponseEntity.noContent().build()

        logoutUseCase.logout(request.refreshToken)

        return ResponseEntity.noContent().build()
    }
}
