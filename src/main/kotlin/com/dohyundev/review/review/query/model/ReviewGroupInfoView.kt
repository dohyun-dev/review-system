package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.group.ReviewGroupStatus
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate

@QueryProjection
open class ReviewGroupInfoView(
    open val id: Long,
    open val name: String,
    open val description: String?,
    open val periodFrom: LocalDate?,
    open val periodTo: LocalDate?,
    open val targetPeriodFrom: LocalDate?,
    open val targetPeriodTo: LocalDate?,
    open val status: ReviewGroupStatus,
) {
    val statusDisplayName: String get() = status.displayName
}
