package com.dohyundev.review.employee.command.domain

enum class EmployeeRole(
    val displayName: String,
) {
    USER("사용자"),
    ADMIN("관리자"),
    REVIEW_RESULT_VIEWER("리뷰 결과 열람자"),
    ;

    val authority: String
        get() = "ROLE_$name"
}