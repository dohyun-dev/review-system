package com.dohyundev.review.employee.query.model

import com.dohyundev.review.employee.command.domain.EmployeeRole
import com.dohyundev.review.employee.command.domain.EmployeeStatus
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate

data class EmployeeListView @QueryProjection constructor(
    val id: Long,
    val no: String,
    val name: String,
    val hireDate: LocalDate,
    val role: EmployeeRole,
    val status: EmployeeStatus,
)
