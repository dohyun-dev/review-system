package com.dohyundev.review.employee.application.port.`in`

import com.dohyundev.review.employee.domain.dto.ChangePasswordCommand
import com.dohyundev.review.employee.domain.entity.Employee

interface ChangePasswordUseCase {
    fun changePassword(id: Long, command: ChangePasswordCommand): Employee
}
