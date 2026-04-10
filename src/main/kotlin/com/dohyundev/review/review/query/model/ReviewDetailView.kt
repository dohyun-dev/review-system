package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.ReviewType

data class ReviewDetailView(
    override val id: Long,
    override val type: ReviewType,
    val sections: List<ReviewSectionView>,
) : ReviewInfoView(
    id = id,
    type = type,
) {
    constructor(review: Review) : this(
        id = review.id!!,
        type = review.type,
        sections = review.sections.sortedBy { it.sortOrder }.map { ReviewSectionView(it) },
    )
}
