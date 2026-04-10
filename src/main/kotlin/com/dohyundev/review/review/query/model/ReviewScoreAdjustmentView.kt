package com.dohyundev.review.review.query.model

import com.dohyundev.review.employee.query.model.EmployeeInfoView
import java.math.BigDecimal

data class ReviewScoreAdjustmentView(
    val id: Long,
    val target: EmployeeInfoView,
    val amount: BigDecimal,
    val reason: String?,
    val adjuster: EmployeeInfoView,
)
