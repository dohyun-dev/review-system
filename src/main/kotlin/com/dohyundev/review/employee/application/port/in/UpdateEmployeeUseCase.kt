package com.dohyundev.review.employee.application.port.`in`

import com.dohyundev.review.employee.domain.dto.UpdateEmployeeCommand
import com.dohyundev.review.employee.domain.entity.Employee

interface UpdateEmployeeUseCase {
    fun update(id: Long, command: UpdateEmployeeCommand): Employee
}
