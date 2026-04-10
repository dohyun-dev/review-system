package com.dohyundev.review.employee.command.port.`in`

import com.dohyundev.review.employee.command.port.`in`.command.ResetPasswordCommand

interface ResetPasswordUseCase {
    fun resetPassword(command: ResetPasswordCommand)
}
