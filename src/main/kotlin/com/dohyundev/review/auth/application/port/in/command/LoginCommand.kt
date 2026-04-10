package com.dohyundev.review.auth.application.port.`in`.command

import com.dohyundev.review.employee.command.domain.EmployeeNo

data class LoginCommand(
    val employeeNo: String,
    val password: String,
) {
    fun toEmployeeNo(): EmployeeNo {
        return EmployeeNo(employeeNo)
    }
}
