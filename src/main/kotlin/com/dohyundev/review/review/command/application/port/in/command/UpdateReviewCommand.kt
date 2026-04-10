package com.dohyundev.review.review.command.application.port.`in`.command

import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.domain.review.command.ReviewSectionCommand

data class UpdateReviewCommand(
    val reviewGroupId: Long,
    val reviewId: Long,
    val type: ReviewType,
    val sections: List<ReviewSectionCommand>,
)
