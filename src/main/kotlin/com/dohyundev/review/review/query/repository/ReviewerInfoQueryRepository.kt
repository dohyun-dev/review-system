package com.dohyundev.review.review.query.repository

import com.dohyundev.review.employee.command.domain.QEmployee
import com.dohyundev.review.review.command.domain.review.QReview.review
import com.dohyundev.review.review.command.domain.reviewer.QReviewer.reviewer
import com.dohyundev.review.review.command.domain.target.QReviewTarget.reviewTarget
import com.dohyundev.review.employee.query.model.QEmployeeInfoView
import com.dohyundev.review.review.query.model.QReviewInfoView
import com.dohyundev.review.review.query.model.QReviewerInfoView
import com.dohyundev.review.review.query.model.ReviewerInfoView
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ReviewerInfoQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun findReviewer(reviewerId: Long): ReviewerInfoView? {
        val reviewerEmployee = QEmployee("reviewerEmployee")
        val targetEmployee = QEmployee("targetEmployee")

        return queryFactory
            .select(
                QReviewerInfoView(
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
            .where(reviewer.id.eq(reviewerId))
            .fetchOne()
    }

    fun findReviewers(reviewGroupId: Long): List<ReviewerInfoView> {
        val reviewerEmployee = QEmployee("reviewerEmployee")
        val targetEmployee = QEmployee("targetEmployee")

        return queryFactory
            .select(
                QReviewerInfoView(
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
}
