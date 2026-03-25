package com.dohyundev.review.employee.application.port.`in`

import com.dohyundev.review.employee.domain.dto.RegisterEmployeeCommand
import com.dohyundev.review.employee.domain.entity.Employee

interface RegisterEmployeeUseCase {
    fun register(command: RegisterEmployeeCommand): Employee
}
