package com.dohyundev.review.organization.member.domain.entity

enum class DepartmentMemberType(
    val displayName: String,
) {
    PRIMARY("주부서"),
    CONCURRENT("겸직부서")
}