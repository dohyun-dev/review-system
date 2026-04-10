package com.dohyundev.review.review.query.model

import com.dohyundev.review.employee.query.model.EmployeeInfoView
import com.dohyundev.review.review.command.domain.reviewer.ReviewerStatus
import com.querydsl.core.annotations.QueryProjection

open class ReviewerInfoView @QueryProjection constructor(
    open val id: Long,
    open val employeeInfo: EmployeeInfoView,
    open val reviewTargetEmployeeInfo: EmployeeInfoView,

    open val reviewInfo: ReviewInfoView,

    open val status: ReviewerStatus,
) {
    val reviewerStatusDisplayName: String get() = status.displayName
}
