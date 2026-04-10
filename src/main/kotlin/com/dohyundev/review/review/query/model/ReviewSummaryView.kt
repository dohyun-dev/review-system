package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.review.ReviewType
import com.querydsl.core.annotations.QueryProjection

data class ReviewSummaryView @QueryProjection constructor(
    override val id: Long,
    override val type: ReviewType,
    val sectionCount: Long,
    val itemCount: Long,
): ReviewInfoView(
    id = id,
    type = type,
)
