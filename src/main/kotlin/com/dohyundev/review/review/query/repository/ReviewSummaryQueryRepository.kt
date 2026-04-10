package com.dohyundev.review.review.query.repository

import com.dohyundev.review.review.command.domain.review.QReview.review
import com.dohyundev.review.review.command.domain.review.QReviewItem.reviewItem
import com.dohyundev.review.review.command.domain.review.QReviewSection.reviewSection
import com.dohyundev.review.review.query.model.QReviewSummaryView
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ReviewSummaryQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun findReviews(reviewGroupId: Long) =
        queryFactory
            .select(
                QReviewSummaryView(
                    review.id,
                    review.type,
                    reviewSection.id.countDistinct(),
                    reviewItem.id.count(),
                )
            )
            .from(review)
            .leftJoin(review.sections, reviewSection)
            .leftJoin(reviewSection.items, reviewItem)
            .where(review.reviewGroup.id.eq(reviewGroupId))
            .groupBy(review.id, review.type)
            .fetch()
}
