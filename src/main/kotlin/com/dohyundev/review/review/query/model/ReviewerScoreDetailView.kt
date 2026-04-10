package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.reviewer.ReviewerStatus
import java.math.BigDecimal

data class ReviewerScoreDetailView(
    val reviewerId: Long,
    val employeeId: Long,
    val status: ReviewerStatus,
    val score: BigDecimal?,
) {
    val statusDisplayName: String get() = status.displayName
}
