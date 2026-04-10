package com.dohyundev.review.review.command.domain.review

enum class ReviewType(
    val displayName: String,
) {
    SELF("셀프리뷰"),
    UPWARD("상향리뷰"),
    DOWNWARD("하향리뷰"), ;
}
