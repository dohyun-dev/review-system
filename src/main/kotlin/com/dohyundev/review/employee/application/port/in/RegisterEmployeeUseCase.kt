package com.dohyundev.review.employee.application.port.`in`

import com.dohyundev.review.employee.domain.dto.RegisterEmployeeCommand
import kr.co.modaoutlet.mgr.employee.domain.Employee

interface RegisterEmployeeUseCase {
    fun create(command: RegisterEmployeeCommand): Employee
}
