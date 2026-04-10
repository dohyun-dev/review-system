package com.dohyundev.review.review.command.domain.review.command

import com.dohyundev.review.review.command.domain.review.ReviewType

open class CreateReviewCommand(
    open val reviewGroupId: Long,
    open val type: ReviewType,
    open val sections: List<ReviewSectionCommand>,
)