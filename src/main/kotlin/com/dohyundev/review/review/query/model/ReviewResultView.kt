package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.review.ReviewType
import java.math.BigDecimal

data class ReviewResultView(
    val reviewId: Long,
    val type: ReviewType,
    val averageScore: BigDecimal,
    val reviewers: List<ReviewerScoreDetailView>,
) {
    val typeDisplayName: String get() = type.displayName
}
