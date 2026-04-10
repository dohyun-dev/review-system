package com.dohyundev.review.employee.command.port.`in`.command

data class ChangePasswordCommand(
    val employeeId: Long,
    val currentPassword: String,
    val newPassword: String
)
