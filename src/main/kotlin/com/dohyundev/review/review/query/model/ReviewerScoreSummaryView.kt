package com.dohyundev.review.review.query.model

import com.dohyundev.review.employee.query.model.EmployeeInfoView
import com.dohyundev.review.review.command.domain.reviewer.ReviewerStatus
import com.querydsl.core.annotations.QueryProjection
import java.math.BigDecimal

data class ReviewerScoreSummaryView @QueryProjection constructor(
    val reviewerId: Long,
    override val employeeInfo: EmployeeInfoView,
    override val reviewTargetEmployeeInfo: EmployeeInfoView,
    override val reviewInfo: ReviewInfoView,
    override val status: ReviewerStatus,
    val score: BigDecimal?,
): ReviewerInfoView(
    id = reviewerId,
    employeeInfo = employeeInfo,
    reviewTargetEmployeeInfo = reviewTargetEmployeeInfo,
    reviewInfo = reviewInfo,
    status = status,
)
