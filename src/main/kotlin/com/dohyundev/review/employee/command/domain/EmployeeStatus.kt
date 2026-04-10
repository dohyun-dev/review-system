package com.dohyundev.review.employee.command.domain

enum class EmployeeStatus(
    val displayName: String,
) {
    ACTIVE("재직"),
    ON_LEAVE("휴직"),
    INACTIVE("퇴사"),
}