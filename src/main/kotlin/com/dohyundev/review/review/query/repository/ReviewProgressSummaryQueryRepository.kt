package com.dohyundev.review.review.query.repository

import com.dohyundev.review.review.command.domain.review.QReview.review
import com.dohyundev.review.review.command.domain.reviewer.QReviewer.reviewer
import com.dohyundev.review.review.command.domain.reviewer.ReviewerStatus
import com.dohyundev.review.review.query.model.QReviewProgressSummaryView
import com.dohyundev.review.review.query.model.ReviewProgressSummaryView
import com.querydsl.core.types.dsl.NumberExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ReviewProgressSummaryQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun findReviewProgress(reviewGroupId: Long): List<ReviewProgressSummaryView> {
        val submittedExpr: NumberExpression<Long> = reviewer.status
            .`when`(ReviewerStatus.SUBMITTED).then(1L).otherwise(0L)
            .castToNum(Long::class.java)
        return queryFactory
            .select(
                QReviewProgressSummaryView(
                    review.id,
                    review.type,
                    reviewer.count().intValue(),
                    submittedExpr.sumLong().intValue(),
                )
            )
            .from(reviewer)
            .join(reviewer.review, review)
            .where(reviewer.reviewGroup.id.eq(reviewGroupId))
            .groupBy(review.id, review.type)
            .fetch()
    }
}
