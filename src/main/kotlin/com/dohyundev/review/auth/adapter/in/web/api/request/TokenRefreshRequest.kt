package com.dohyundev.review.auth.adapter.`in`.web.api.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class TokenRefreshRequest(
    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    val refreshToken: String? = null,
)
