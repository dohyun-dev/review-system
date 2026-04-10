package com.dohyundev.review.review.query.controller.dto

import com.dohyundev.review.review.query.model.ReviewGroupInfoView
import com.dohyundev.review.review.query.model.ReviewProgressSummaryView
import com.dohyundev.review.review.query.model.ReviewerProgressInfoView

data class ReviewGroupProgressResponse(
    val reviewGroupInfo: ReviewGroupInfoView,
    val reviews: List<ReviewProgressSummaryView>,
    val reviewers: List<ReviewerProgressInfoView>,
) {
}