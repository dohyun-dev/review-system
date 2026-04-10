package com.dohyundev.review.review.command.domain.reviewer

enum class ReviewerStatus(val displayName: String) {
    WAIT("대기"),
    DRAFT("임시저장"),
    NOT_SUBMITTED("미제출"),
    SUBMITTED("제출"),
}
