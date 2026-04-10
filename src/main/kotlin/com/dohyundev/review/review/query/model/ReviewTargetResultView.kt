package com.dohyundev.review.review.query.model

import com.dohyundev.review.employee.query.model.EmployeeInfoView

data class ReviewTargetResultView(
    override val id: Long,
    override val employeeInfo: EmployeeInfoView,
    val reviewScores: List<ReviewTargetReviewScoreView>,
    val adjustments: List<ReviewScoreAdjustmentResultView>,
    val reviewerScores: List<ReviewerScoreSummaryView>,
): ReviewTargetInfoView(
    id = id,
    employeeInfo = employeeInfo,
)
