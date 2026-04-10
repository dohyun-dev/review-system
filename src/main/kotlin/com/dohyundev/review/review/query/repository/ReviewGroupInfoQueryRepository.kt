package com.dohyundev.review.review.query.repository

import com.dohyundev.review.review.command.domain.group.QReviewGroup.reviewGroup
import com.dohyundev.review.review.query.model.QReviewGroupInfoView
import com.dohyundev.review.review.query.model.ReviewGroupInfoView
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ReviewGroupInfoQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun findAll(pageable: Pageable): Page<ReviewGroupInfoView> {
        val content = queryFactory
            .select(
                QReviewGroupInfoView(
                    reviewGroup.id,
                    reviewGroup.name,
                    reviewGroup.description,
                    reviewGroup.period.from,
                    reviewGroup.period.to,
                    reviewGroup.targetPeriod.from,
                    reviewGroup.targetPeriod.to,
                    reviewGroup.status,
                )
            )
            .from(reviewGroup)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(reviewGroup.count())
            .from(reviewGroup)
            .fetchOne()!!

        return PageImpl(content, pageable, total)
    }

    fun findInfo(id: Long) =
        queryFactory
            .select(
                QReviewGroupInfoView(
                    reviewGroup.id,
                    reviewGroup.name,
                    reviewGroup.description,
                    reviewGroup.period.from,
                    reviewGroup.period.to,
                    reviewGroup.targetPeriod.from,
                    reviewGroup.targetPeriod.to,
                    reviewGroup.status,
                )
            )
            .from(reviewGroup)
            .where(reviewGroup.id.eq(id))
            .fetchOne()
}
