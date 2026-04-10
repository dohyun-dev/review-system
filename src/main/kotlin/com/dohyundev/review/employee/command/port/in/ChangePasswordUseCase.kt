package com.dohyundev.review.employee.command.port.`in`

import com.dohyundev.review.employee.command.port.`in`.command.ChangePasswordCommand

interface ChangePasswordUseCase {
    fun changePassword(command: ChangePasswordCommand)
}
