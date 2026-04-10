package com.dohyundev.review.employee.command.adapter.`in`.web.api.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class ResetPasswordRequest(
    @field:NotBlank(message = "새 비밀번호는 비어 있을 수 없습니다.")
    @Schema(description = "새 비밀번호", example = "newpass1")
    val newPassword: String
)
