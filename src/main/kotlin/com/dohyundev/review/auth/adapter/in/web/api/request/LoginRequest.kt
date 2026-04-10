package com.dohyundev.review.auth.adapter.`in`.web.api.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class LoginRequest(
    @field:NotBlank(message = "사원번호는 비어 있을 수 없습니다.")
    @field:Pattern(regexp = "\\d{6}", message = "사원번호는 6자리 숫자여야 합니다.")
    @Schema(description = "사원번호 (6자리)", example = "000001")
    val employeeNo: String,

    @field:NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
    @Schema(description = "비밀번호", example = "password1")
    val password: String,
)
