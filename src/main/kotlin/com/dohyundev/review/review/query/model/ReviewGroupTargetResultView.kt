package com.dohyundev.review.review.query.model

import com.dohyundev.review.employee.query.model.EmployeeInfoView

data class ReviewGroupTargetResultView(
    override val id: Long,
    override val employeeInfo: EmployeeInfoView,
    val reviewScores: List<ReviewTargetReviewScoreView>,
): ReviewTargetInfoView(id = id, employeeInfo = employeeInfo)