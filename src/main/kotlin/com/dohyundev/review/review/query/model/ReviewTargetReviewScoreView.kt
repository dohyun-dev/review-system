package com.dohyundev.review.review.query.model

import com.querydsl.core.annotations.QueryProjection

class ReviewTargetReviewScoreView @QueryProjection constructor(
    val targetInfo: ReviewTargetInfoView,
    val reviewInfo: ReviewInfoView,
    val score: Double,
) {
}