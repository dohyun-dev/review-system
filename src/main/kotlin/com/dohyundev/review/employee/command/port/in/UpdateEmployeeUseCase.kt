package com.dohyundev.review.employee.command.port.`in`

import com.dohyundev.review.employee.command.port.`in`.command.UpdateEmployeeCommand

interface UpdateEmployeeUseCase {
    fun update(command: UpdateEmployeeCommand)
}
