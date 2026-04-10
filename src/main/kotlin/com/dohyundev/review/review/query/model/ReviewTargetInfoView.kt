package com.dohyundev.review.review.query.model

import com.dohyundev.review.employee.query.model.EmployeeInfoView
import com.querydsl.core.annotations.QueryProjection

open class ReviewTargetInfoView @QueryProjection constructor(
    open val id: Long,
    open val employeeInfo: EmployeeInfoView,
)
