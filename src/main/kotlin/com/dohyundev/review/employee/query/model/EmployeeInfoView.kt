package com.dohyundev.review.employee.query.model

import com.dohyundev.review.employee.command.domain.Employee
import com.querydsl.core.annotations.QueryProjection

data class EmployeeInfoView @QueryProjection constructor(
    val id: Long,
    val no: String,
    val name: String
) {
    constructor(employee: Employee) : this(
        id = employee.id!!,
        no = employee.no.toString(),
        name = employee.name
    )
}
