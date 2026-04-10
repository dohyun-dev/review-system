package com.dohyundev.review.review.query.controller.dto

import com.dohyundev.review.review.query.model.ReviewGroupInfoView
import com.dohyundev.review.review.query.model.ReviewSummaryView
import com.dohyundev.review.review.query.model.ReviewTargetInfoView
import com.dohyundev.review.review.query.model.ReviewerInfoView

data class ReviewGroupDetailResponse(
    val info: ReviewGroupInfoView,
    val reviews: List<ReviewSummaryView>,
    val targets: List<ReviewTargetInfoView>,
    val reviewers: List<ReviewerInfoView>,
)