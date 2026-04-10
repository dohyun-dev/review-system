package com.dohyundev.review.review.query.repository

import com.dohyundev.review.employee.command.domain.QEmployee
import com.dohyundev.review.employee.command.domain.QEmployee.employee
import com.dohyundev.review.employee.query.model.QEmployeeInfoView
import com.dohyundev.review.review.command.domain.review.QReview.review
import com.dohyundev.review.review.command.domain.reviewer.QReviewer.reviewer
import com.dohyundev.review.review.command.domain.score.QReviewScoreAdjustment.reviewScoreAdjustment
import com.dohyundev.review.review.command.domain.score.QReviewerScore.reviewerScore
import com.dohyundev.review.review.command.domain.target.QReviewTarget.reviewTarget
import com.dohyundev.review.review.query.model.QReviewInfoView
import com.dohyundev.review.review.query.model.QReviewTargetInfoView
import com.dohyundev.review.review.query.model.QReviewTargetReviewScoreView
import com.dohyundev.review.review.query.model.QReviewerScoreSummaryView
import com.dohyundev.review.review.query.model.ReviewScoreAdjustmentResultView
import com.dohyundev.review.review.query.model.ReviewTargetInfoView
import com.dohyundev.review.review.query.model.ReviewTargetResultView
import com.dohyundev.review.review.query.model.ReviewTargetReviewScoreView
import com.dohyundev.review.review.query.model.ReviewerScoreSummaryView
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ReviewTargetResultQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun findResults(reviewGroupId: Long): List<ReviewTargetResultView> {
        val targets = findTargets(reviewGroupId)
        val reviewScoresByTargetId = findReviewScoresByTargetId(reviewGroupId)
        val adjustmentsByTargetId = findAdjustmentsByTargetId(reviewGroupId)
        val reviewerScoresByTargetId = findReviewerScoresByTargetId(reviewGroupId)

        return targets.map { target ->
            ReviewTargetResultView(
                id = target.id,
                employeeInfo = target.employeeInfo,
                reviewScores = reviewScoresByTargetId[target.id].orEmpty(),
                adjustments = adjustmentsByTargetId[target.id].orEmpty(),
                reviewerScores = reviewerScoresByTargetId[target.id].orEmpty(),
            )
        }
    }

    fun findTargets(reviewGroupId: Long): List<ReviewTargetInfoView> {
        return queryFactory
            .select(
                QReviewTargetInfoView(
                    reviewTarget.id,
                    QEmployeeInfoView(
                        reviewTarget.employeeId,
                        employee.no.number,
                        employee.name,
                    ),
                )
            )
            .from(reviewTarget)
            .join(employee).on(employee.id.eq(reviewTarget.employeeId))
            .where(reviewTarget.reviewGroup.id.eq(reviewGroupId))
            .fetch()
    }

    private fun findReviewScoresByTargetId(reviewGroupId: Long): Map<Long, List<ReviewTargetReviewScoreView>> {
        val projection = QReviewTargetReviewScoreView(
            QReviewTargetInfoView(
                reviewTarget.id,
                QEmployeeInfoView(reviewTarget.employeeId, employee.no.number, employee.name),
            ),
            QReviewInfoView(review.id, review.type),
            reviewerScore.score.avg().coalesce(0.0),
        )
        return queryFactory
            .select(reviewTarget.id, projection)
            .from(reviewer)
            .join(reviewer.review, review)
            .join(reviewer.reviewTarget, reviewTarget)
            .join(employee).on(employee.id.eq(reviewTarget.employeeId))
            .leftJoin(reviewerScore).on(reviewerScore.reviewer.eq(reviewer))
            .where(reviewer.reviewGroup.id.eq(reviewGroupId))
            .groupBy(reviewTarget.id, reviewTarget.employeeId, employee.no.number, employee.name, review.id, review.type)
            .fetch()
            .groupBy { it[reviewTarget.id]!! }
            .mapValues { (_, rows) -> rows.mapNotNull { it[projection] } }
    }

    private fun findAdjustmentsByTargetId(reviewGroupId: Long): Map<Long, List<ReviewScoreAdjustmentResultView>> {
        val adjusterEmployee = QEmployee("adjusterEmployee")
        val adjusterInfoExpr = QEmployeeInfoView(adjusterEmployee.id, adjusterEmployee.no.number, adjusterEmployee.name)
        return queryFactory
            .from(reviewScoreAdjustment)
            .join(adjusterEmployee).on(adjusterEmployee.id.eq(reviewScoreAdjustment.adjuster.employeeId))
            .where(reviewScoreAdjustment.reviewGroup.id.eq(reviewGroupId))
            .select(
                reviewScoreAdjustment.reviewTarget.id,
                reviewScoreAdjustment.amount,
                reviewScoreAdjustment.reason,
                adjusterInfoExpr,
            )
            .fetch()
            .groupBy { it[reviewScoreAdjustment.reviewTarget.id]!! }
            .mapValues { (_, rows) ->
                rows.map {
                    ReviewScoreAdjustmentResultView(
                        amount = it[reviewScoreAdjustment.amount]!!,
                        reason = it[reviewScoreAdjustment.reason],
                        adjuster = it[adjusterInfoExpr]!!,
                    )
                }
            }
    }

    private fun findReviewerScoresByTargetId(reviewGroupId: Long): Map<Long, List<ReviewerScoreSummaryView>> {
        val reviewerEmployee = QEmployee("reviewerEmployee")
        val targetEmployee = QEmployee("targetEmployee")
        val projection = QReviewerScoreSummaryView(
            reviewer.id,
            QEmployeeInfoView(reviewer.employeeId, reviewerEmployee.no.number, reviewerEmployee.name),
            QEmployeeInfoView(reviewTarget.employeeId, targetEmployee.no.number, targetEmployee.name),
            QReviewInfoView(review.id, review.type),
            reviewer.status,
            reviewerScore.score,
        )
        return queryFactory
            .select(reviewTarget.id, projection)
            .from(reviewer)
            .join(reviewer.review, review)
            .join(reviewer.reviewTarget, reviewTarget)
            .join(reviewerEmployee).on(reviewerEmployee.id.eq(reviewer.employeeId))
            .join(targetEmployee).on(targetEmployee.id.eq(reviewTarget.employeeId))
            .leftJoin(reviewerScore).on(reviewerScore.reviewer.eq(reviewer))
            .where(reviewer.reviewGroup.id.eq(reviewGroupId))
            .fetch()
            .groupBy { it[reviewTarget.id]!! }
            .mapValues { (_, rows) -> rows.mapNotNull { it[projection] } }
    }
}
