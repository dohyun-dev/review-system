package com.dohyundev.review.employee.domain.dto

import com.dohyundev.review.employee.domain.entity.EmployeeRole
import kr.co.modaoutlet.mgr.employee.domain.EmployeeStatus
import java.time.LocalDate

data class UpdateEmployeeCommand(
    val name: String,
    val role: EmployeeRole,
    val hireDate: LocalDate,
    val status: EmployeeStatus,
)