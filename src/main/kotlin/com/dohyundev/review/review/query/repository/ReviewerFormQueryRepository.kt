package com.dohyundev.review.review.query.repository

import com.dohyundev.review.review.command.domain.answer.QReviewAnswerItem.reviewAnswerItem
import com.dohyundev.review.review.command.domain.answer.QReviewerAnswer.reviewerAnswer
import com.dohyundev.review.review.command.domain.review.QReview.review
import com.dohyundev.review.review.command.domain.review.QReviewItem.reviewItem
import com.dohyundev.review.review.command.domain.review.QReviewSection.reviewSection
import com.dohyundev.review.review.query.factory.ReviewerFormItemViewFactory
import com.dohyundev.review.review.query.model.ReviewerFormSectionView
import com.dohyundev.review.review.query.model.ReviewerFormView
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ReviewerFormQueryRepository(
    private val queryFactory: JPAQueryFactory,
    private val reviewerInfoQueryRepository: ReviewerInfoQueryRepository,
) {
    fun findForm(reviewerId: Long): ReviewerFormView? {
        val reviewerInfo = reviewerInfoQueryRepository.findReviewer(reviewerId) ?: return null
        val sections = findSections(reviewerInfo.reviewInfo.id, reviewerId) ?: return null

        return ReviewerFormView(
            reviewerInfo = reviewerInfo,
            sections = sections,
        )
    }

    private fun findSections(reviewId: Long, reviewerId: Long): List<ReviewerFormSectionView>? {
        val reviewEntity = queryFactory
            .selectFrom(review)
            .leftJoin(review.sections, reviewSection).fetchJoin()
            .leftJoin(reviewSection.items, reviewItem).fetchJoin()
            .where(review.id.eq(reviewId))
            .fetchOne() ?: return null

        val answerItems = (queryFactory
            .selectFrom(reviewerAnswer)
            .leftJoin(reviewerAnswer.items, reviewAnswerItem).fetchJoin()
            .where(reviewerAnswer.reviewer.id.eq(reviewerId))
            .fetchOne()
            ?.items ?: emptyList())
            .associateBy { it.reviewItemId }

        return reviewEntity.sections
            .sortedBy { it.sortOrder }
            .map { section ->
                ReviewerFormSectionView(
                    id = section.id!!,
                    title = section.title,
                    description = section.description,
                    sortOrder = section.sortOrder,
                    items = section.items
                        .sortedBy { it.sortOrder }
                        .map { ReviewerFormItemViewFactory.create(it, answerItems[it.id]) },
                )
            }
    }
}
