package com.dohyundev.review.employee.domain.dto

import com.dohyundev.review.employee.domain.entity.EmployeeRole
import java.time.LocalDate

data class RegisterEmployeeCommand(
    val no: String,
    val name: String,
    val hireDate: LocalDate,
    val role: EmployeeRole = EmployeeRole.USER,
)
