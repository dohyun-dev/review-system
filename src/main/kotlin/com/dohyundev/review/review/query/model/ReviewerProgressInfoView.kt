package com.dohyundev.review.review.query.model

import com.dohyundev.review.employee.query.model.EmployeeInfoView

data class ReviewerProgressInfoView(
    val id: Long,
    val employeeInfo: EmployeeInfoView,
    val reviewProgressSummaries: List<ReviewProgressSummaryView>,
    val reviews: List<ReviewerReviewProgressDetailView>,
)
