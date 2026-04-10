package com.dohyundev.review.review.command.adapter.`in`.web.api.request

import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.application.port.`in`.command.AppendReviewCommand
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class CreateReviewRequest(
    @field:NotNull(message = "리뷰 타입은 필수입니다.")
    var type: ReviewType? = null,
    @field:NotEmpty(message = "섹션은 하나 이상 있어야 합니다.")
    @field:Valid
    val sections: List<ReviewSectionRequest> = emptyList(),
) {
    fun toCommand(reviewGroupId: Long) = AppendReviewCommand(
        reviewGroupId = reviewGroupId,
        type = type!!,
        sections = sections.map { it.toCommand() },
    )
}
