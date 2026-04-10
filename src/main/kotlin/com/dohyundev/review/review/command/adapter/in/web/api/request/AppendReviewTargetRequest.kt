package com.dohyundev.review.review.command.adapter.`in`.web.api.request

import com.dohyundev.review.review.command.application.port.`in`.command.CreateReviewTargetCommand
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class AppendReviewTargetRequest(
    @field:NotNull(message = "사원 ID는 필수입니다.")
    @Schema(description = "사원 ID")
    val employeeId: Long? = null,
) {
    fun toCommand(reviewGroupId: Long) = CreateReviewTargetCommand(
        reviewGroupId = reviewGroupId,
        employeeId = employeeId!!,
    )
}