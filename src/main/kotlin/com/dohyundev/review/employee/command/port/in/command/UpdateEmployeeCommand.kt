package com.dohyundev.review.employee.command.port.`in`.command

import com.dohyundev.review.employee.command.domain.EmployeeRole
import com.dohyundev.review.employee.command.domain.EmployeeStatus
import java.time.LocalDate

data class UpdateEmployeeCommand(
    val employeeId: Long,
    val name: String,
    val hireDate: LocalDate,
    val role: EmployeeRole,
    val status: EmployeeStatus
)
