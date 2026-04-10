package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.reviewer.ReviewerStatus

data class ReviewTargetStatusView(
    val reviewTargetId: Long,
    val status: ReviewerStatus,
) {
    val statusDisplayName: String get() = status.displayName
}
