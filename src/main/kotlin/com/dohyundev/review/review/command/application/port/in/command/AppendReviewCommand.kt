package com.dohyundev.review.review.command.application.port.`in`.command

import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.domain.review.command.CreateReviewCommand
import com.dohyundev.review.review.command.domain.review.command.ReviewSectionCommand

data class AppendReviewCommand(
    override val reviewGroupId: Long,
    override val type: ReviewType,
    override val sections: List<ReviewSectionCommand>,
) : CreateReviewCommand(reviewGroupId, type, sections)