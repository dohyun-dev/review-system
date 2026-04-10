package com.dohyundev.review.employee.command.port.`in`.command

import com.dohyundev.review.employee.command.domain.EmployeeRole
import java.time.LocalDate

data class RegisterEmployeeCommand(
    val employeeNo: String,
    val name: String,
    val hireDate: LocalDate,
    val role: EmployeeRole
)