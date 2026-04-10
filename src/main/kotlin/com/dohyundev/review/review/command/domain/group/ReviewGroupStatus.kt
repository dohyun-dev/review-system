package com.dohyundev.review.review.command.domain.group

enum class ReviewGroupStatus(
    val displayName: String,
) {
    PREPARING("준비중"),
    IN_PROGRESS("진행중"),
    CLOSED("마감"),
}
