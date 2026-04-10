package com.dohyundev.review.review.query.repository

import com.dohyundev.review.employee.command.domain.QEmployee
import com.dohyundev.review.employee.query.model.QEmployeeInfoView
import com.dohyundev.review.review.command.domain.review.QReview.review
import com.dohyundev.review.review.command.domain.reviewer.QReviewer.reviewer
import com.dohyundev.review.review.command.domain.reviewer.ReviewerStatus
import com.dohyundev.review.review.command.domain.target.QReviewTarget.reviewTarget
import com.dohyundev.review.review.query.model.QReviewInfoView
import com.dohyundev.review.review.query.model.QReviewProgressSummaryView
import com.dohyundev.review.review.query.model.QReviewerReviewProgressDetailView
import com.dohyundev.review.review.query.model.ReviewProgressSummaryView
import com.dohyundev.review.review.query.model.ReviewerProgressInfoView
import com.dohyundev.review.review.query.model.ReviewerReviewProgressDetailView
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ReviewerProgressInfoQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun findReviewerProgress(reviewGroupId: Long): List<ReviewerProgressInfoView> {
        val reviews = findReviews(reviewGroupId)
        val progressByEmployee = findProgressByEmployee(reviewGroupId)

        return reviews
            .groupBy { it.employeeInfo.id }
            .map { (employeeId, employeeReviews) ->
                ReviewerProgressInfoView(
                    id = employeeId,
                    employeeInfo = employeeReviews.first().employeeInfo,
                    reviewProgressSummaries = progressByEmployee[employeeId] ?: emptyList(),
                    reviews = employeeReviews,
                )
            }
    }

    private fun findReviews(reviewGroupId: Long): List<ReviewerReviewProgressDetailView> {
        val reviewerEmployee = QEmployee("reviewerEmployee")
        val targetEmployee = QEmployee("targetEmployee")

        return queryFactory
            .select(
                QReviewerReviewProgressDetailView(
                    reviewer.id,
                    QEmployeeInfoView(
                        reviewer.employeeId,
                        reviewerEmployee.no.number,
                        reviewerEmployee.name,
                    ),
                    QEmployeeInfoView(
                        reviewTarget.employeeId,
                        targetEmployee.no.number,
                        targetEmployee.name,
                    ),
                    QReviewInfoView(
                        review.id,
                        review.type,
                    ),
                    reviewer.status,
                )
            )
            .from(reviewer)
            .join(reviewer.review, review)
            .join(reviewer.reviewTarget, reviewTarget)
            .join(reviewerEmployee).on(reviewerEmployee.id.eq(reviewer.employeeId))
            .join(targetEmployee).on(targetEmployee.id.eq(reviewTarget.employeeId))
            .where(reviewer.reviewGroup.id.eq(reviewGroupId))
            .fetch()
    }

    private fun findProgressByEmployee(reviewGroupId: Long): Map<Long, List<ReviewProgressSummaryView>> {
        val progressSummaryExpr = QReviewProgressSummaryView(
            review.id,
            review.type,
            reviewer.count().intValue(),
            reviewer.status
                .`when`(ReviewerStatus.SUBMITTED).then(1L)
                .otherwise(0L)
                .castToNum(Long::class.java)
                .sumLong().intValue(),
        )
        return queryFactory
            .select(reviewer.employeeId, progressSummaryExpr)
            .from(reviewer)
            .join(reviewer.review, review)
            .where(reviewer.reviewGroup.id.eq(reviewGroupId))
            .groupBy(reviewer.employeeId, review.id, review.type)
            .fetch()
            .groupBy({ it[reviewer.employeeId]!! }, { it[progressSummaryExpr]!! })
    }
}
