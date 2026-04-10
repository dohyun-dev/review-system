package com.dohyundev.review.employee.command.domain

import jakarta.persistence.Embeddable

@Embeddable
data class EmployeeNo (
    val number: String
) {
    init {
        require(number.isNotBlank())
    }

    companion object {
        fun create(number: String): EmployeeNo {
            require(number.matches(Regex("\\d{6}"))) { "사원번호는 6자리 숫자여야 합니다." }
            return EmployeeNo(number)
        }
    }
}
