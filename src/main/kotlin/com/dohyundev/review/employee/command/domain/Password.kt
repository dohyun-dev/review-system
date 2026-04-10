package com.dohyundev.review.employee.command.domain

import jakarta.persistence.Embeddable

@Embeddable
class Password(
    val value: String
) {
    init {
        require(value.isNotBlank()) { "비밀번호는 비어 있을 수 없습니다." }
    }

    companion object {
        const val DEFAULT_PASSWORD = "0000"
    }
}