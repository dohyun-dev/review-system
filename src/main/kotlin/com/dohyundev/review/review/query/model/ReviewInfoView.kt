package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.review.ReviewType
import com.querydsl.core.annotations.QueryProjection

open class ReviewInfoView @QueryProjection constructor(
    open val id: Long,
    open val type: ReviewType,
) {
    val typeDisplayName: String get() = type.displayName
}