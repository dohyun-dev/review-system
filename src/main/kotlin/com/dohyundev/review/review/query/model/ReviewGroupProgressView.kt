package com.dohyundev.review.review.query.model

data class ReviewGroupProgressView(
    val reviews: List<ReviewProgressSummaryView>,
    val reviewers: List<ReviewerProgressInfoView>,
)
