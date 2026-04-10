package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.review.ReviewType
import com.querydsl.core.annotations.QueryProjection

data class ReviewProgressSummaryView @QueryProjection constructor(
    override val id: Long,
    override val type: ReviewType,
    val totalCount: Int,
    val submittedCount: Int,
): ReviewInfoView(
    id = id,
    type = type,
) {
    val submittedPercent: Double get() =
        if (totalCount == 0) 0.0
        else Math.round(submittedCount.toDouble() / totalCount * 1000) / 10.0
}
