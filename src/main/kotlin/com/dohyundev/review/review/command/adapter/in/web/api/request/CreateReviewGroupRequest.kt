package com.dohyundev.review.review.command.adapter.`in`.web.api.request

import com.dohyundev.review.common.DateRangeRequest
import com.dohyundev.review.review.command.application.port.`in`.command.CreateReviewGroupCommand
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class CreateReviewGroupRequest(
    @field:NotBlank(message = "리뷰 그룹명은 비어 있을 수 없습니다.")
    @Schema(description = "리뷰 그룹명", example = "2026 상반기 리뷰")
    val name: String = "",

    @Schema(description = "설명", example = "상반기 성과 리뷰")
    val description: String? = null,

    @Schema(description = "리뷰 기간")
    val period: DateRangeRequest? = null,

    @Schema(description = "평가 대상 기간")
    val targetPeriod: DateRangeRequest? = null,
) {
    fun toCommand(): CreateReviewGroupCommand = CreateReviewGroupCommand(
        name = name,
        description = description,
        period = period?.toDateRange(),
        targetPeriod = targetPeriod?.toDateRange(),
    )
}
