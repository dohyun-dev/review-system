package com.dohyundev.review.review.command.adapter.`in`.web.api.request

import com.dohyundev.review.review.command.application.port.`in`.command.AppendReviewerCommand
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class AppendReviewerRequest(
    @field:NotNull(message = "리뷰 ID는 필수입니다.")
    @Schema(description = "리뷰 ID")
    val reviewId: Long? = null,

    @field:NotNull(message = "리뷰 대상자 ID는 필수입니다.")
    @Schema(description = "리뷰 대상자 ID")
    val reviewTargetId: Long? = null,

    @field:NotNull(message = "사원 ID는 필수입니다.")
    @Schema(description = "리뷰어 사원 ID")
    val employeeId: Long? = null,
) {
    fun toCommand(reviewGroupId: Long) = AppendReviewerCommand(
        reviewGroupId = reviewGroupId,
        reviewId = reviewId!!,
        reviewTargetId = reviewTargetId!!,
        employeeId = employeeId!!,
    )
}