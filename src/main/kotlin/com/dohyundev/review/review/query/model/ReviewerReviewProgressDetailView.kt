package com.dohyundev.review.review.query.model

import com.dohyundev.review.employee.query.model.EmployeeInfoView
import com.dohyundev.review.review.command.domain.reviewer.ReviewerStatus
import com.querydsl.core.annotations.QueryProjection

data class ReviewerReviewProgressDetailView @QueryProjection constructor(
    override val id: Long,
    override val employeeInfo: EmployeeInfoView,
    override val reviewTargetEmployeeInfo: EmployeeInfoView,
    override val reviewInfo: ReviewInfoView,
    override val status: ReviewerStatus,
): ReviewerInfoView(
    id = id,
    employeeInfo = employeeInfo,
    reviewTargetEmployeeInfo = reviewTargetEmployeeInfo,
    reviewInfo = reviewInfo,
    status = status,
)
