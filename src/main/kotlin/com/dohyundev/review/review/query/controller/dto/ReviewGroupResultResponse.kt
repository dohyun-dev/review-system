package com.dohyundev.review.review.query.controller.dto

import com.dohyundev.review.review.query.model.ReviewGroupInfoView
import com.dohyundev.review.review.query.model.ReviewTargetResultView

data class ReviewGroupResultResponse(
    val reviewGroupInfo: ReviewGroupInfoView,
    val targets: List<ReviewTargetResultView>,
)