package com.dohyundev.review.employee.command.port.`in`

import com.dohyundev.review.employee.command.port.`in`.command.RegisterEmployeeCommand

interface RegisterEmployeeUseCase {
    fun register(command: RegisterEmployeeCommand): Long
}
