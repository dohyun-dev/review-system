package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.employee.command.domain.EmployeeRole
import com.dohyundev.review.employee.command.domain.exception.EmployeeNotFoundException
import com.dohyundev.review.employee.command.port.out.EmployeeRepository
import com.dohyundev.review.employee.fixture.EmployeeFixture
import com.dohyundev.review.review.command.application.port.`in`.AddReviewScoreAdjustmentUseCase
import com.dohyundev.review.review.command.application.port.`in`.RemoveReviewScoreAdjustmentUseCase
import com.dohyundev.review.review.command.application.port.`in`.command.AddReviewScoreAdjustmentCommand
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.port.out.ReviewScoreAdjustmentRepository
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository
import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotFoundException
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotClosedException
import com.dohyundev.review.review.command.domain.score.Adjuster
import com.dohyundev.review.review.command.domain.score.ReviewScoreAdjustment
import com.dohyundev.review.review.command.domain.score.ReviewScoreAdjustmentNotFoundException
import com.dohyundev.review.review.command.domain.target.ReviewTarget
import com.dohyundev.review.review.command.domain.target.ReviewTargetNotFoundException
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewTargetFixture
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
class ReviewScoreAdjustmentCommandServiceTest {

    @Autowired lateinit var addReviewScoreAdjustmentUseCase: AddReviewScoreAdjustmentUseCase
    @Autowired lateinit var removeReviewScoreAdjustmentUseCase: RemoveReviewScoreAdjustmentUseCase
    @Autowired lateinit var reviewGroupRepository: ReviewGroupRepository
    @Autowired lateinit var reviewTargetRepository: ReviewTargetRepository
    @Autowired lateinit var reviewScoreAdjustmentRepository: ReviewScoreAdjustmentRepository
    @Autowired lateinit var employeeRepository: EmployeeRepository

    private lateinit var closedReviewGroup: ReviewGroup
    private lateinit var reviewTarget: ReviewTarget
    private var adminEmployeeId: Long = 0L

    @BeforeTest
    fun setUp() {
        closedReviewGroup = reviewGroupRepository.saveAndFlush(ReviewGroupFixture.reviewGroup())
        reviewTarget = reviewTargetRepository.saveAndFlush(
            ReviewTargetFixture.target(reviewGroup = closedReviewGroup, employeeId = 9999L)
        )
        closedReviewGroup.start { }
        closedReviewGroup.close()
        closedReviewGroup = reviewGroupRepository.saveAndFlush(closedReviewGroup)
        adminEmployeeId = employeeRepository.saveAndFlush(
            EmployeeFixture.employee(role = EmployeeRole.ADMIN)
        ).id!!
    }

    @Test
    fun `점수 가감을 추가한다`() {
        val adjustmentId = addReviewScoreAdjustmentUseCase.addReviewScoreAdjustment(
            AddReviewScoreAdjustmentCommand(
                reviewGroupId = closedReviewGroup.id!!,
                reviewTargetId = reviewTarget.id!!,
                adjusterId = adminEmployeeId,
                amount = BigDecimal("5.0"),
                reason = "추가 가감 사유",
            )
        )
        assertTrue(reviewScoreAdjustmentRepository.findById(adjustmentId).isPresent)
    }

    @Test
    fun `열람자 권한의 사원도 점수 가감을 추가할 수 있다`() {
        val viewerId = employeeRepository.saveAndFlush(
            EmployeeFixture.employee(no = "000002", role = EmployeeRole.REVIEW_RESULT_VIEWER)
        ).id!!
        val adjustmentId = addReviewScoreAdjustmentUseCase.addReviewScoreAdjustment(
            AddReviewScoreAdjustmentCommand(
                reviewGroupId = closedReviewGroup.id!!,
                reviewTargetId = reviewTarget.id!!,
                adjusterId = viewerId,
                amount = BigDecimal("3.0"),
            )
        )
        assertTrue(reviewScoreAdjustmentRepository.findById(adjustmentId).isPresent)
    }

    @Test
    fun `권한이 없는 사원이 점수 가감을 추가하면 예외가 발생한다`() {
        val userId = employeeRepository.saveAndFlush(
            EmployeeFixture.employee(no = "000002", role = EmployeeRole.USER)
        ).id!!
        assertFailsWith<IllegalStateException> {
            addReviewScoreAdjustmentUseCase.addReviewScoreAdjustment(
                AddReviewScoreAdjustmentCommand(
                    reviewGroupId = closedReviewGroup.id!!,
                    reviewTargetId = reviewTarget.id!!,
                    adjusterId = userId,
                    amount = BigDecimal("5.0"),
                )
            )
        }
    }

    @Test
    fun `존재하지 않는 사원이 점수 가감을 추가하면 예외가 발생한다`() {
        assertFailsWith<EmployeeNotFoundException> {
            addReviewScoreAdjustmentUseCase.addReviewScoreAdjustment(
                AddReviewScoreAdjustmentCommand(
                    reviewGroupId = closedReviewGroup.id!!,
                    reviewTargetId = reviewTarget.id!!,
                    adjusterId = -1L,
                    amount = BigDecimal("5.0"),
                )
            )
        }
    }

    @Test
    fun `존재하지 않는 리뷰 그룹에 점수 가감을 추가하면 예외가 발생한다`() {
        assertFailsWith<ReviewGroupNotFoundException> {
            addReviewScoreAdjustmentUseCase.addReviewScoreAdjustment(
                AddReviewScoreAdjustmentCommand(
                    reviewGroupId = -1L,
                    reviewTargetId = reviewTarget.id!!,
                    adjusterId = adminEmployeeId,
                    amount = BigDecimal("5.0"),
                )
            )
        }
    }

    @Test
    fun `존재하지 않는 리뷰 대상자에 점수 가감을 추가하면 예외가 발생한다`() {
        assertFailsWith<ReviewTargetNotFoundException> {
            addReviewScoreAdjustmentUseCase.addReviewScoreAdjustment(
                AddReviewScoreAdjustmentCommand(
                    reviewGroupId = closedReviewGroup.id!!,
                    reviewTargetId = -1L,
                    adjusterId = adminEmployeeId,
                    amount = BigDecimal("5.0"),
                )
            )
        }
    }

    @Test
    fun `종료되지 않은 리뷰 그룹에 점수 가감을 추가하면 예외가 발생한다`() {
        var inProgressGroup = reviewGroupRepository.saveAndFlush(ReviewGroupFixture.reviewGroup())
        val inProgressTarget = reviewTargetRepository.saveAndFlush(
            ReviewTargetFixture.target(reviewGroup = inProgressGroup, employeeId = 8888L)
        )
        inProgressGroup.start { }
        inProgressGroup = reviewGroupRepository.saveAndFlush(inProgressGroup)
        assertFailsWith<ReviewGroupNotClosedException> {
            addReviewScoreAdjustmentUseCase.addReviewScoreAdjustment(
                AddReviewScoreAdjustmentCommand(
                    reviewGroupId = inProgressGroup.id!!,
                    reviewTargetId = inProgressTarget.id!!,
                    adjusterId = adminEmployeeId,
                    amount = BigDecimal("5.0"),
                )
            )
        }
    }

    @Test
    fun `점수 가감을 삭제한다`() {
        val adjustment = reviewScoreAdjustmentRepository.saveAndFlush(
            ReviewScoreAdjustment(
                reviewGroup = closedReviewGroup,
                reviewTarget = reviewTarget,
                adjuster = Adjuster(adminEmployeeId),
                amount = BigDecimal("5.0"),
            )
        )
        removeReviewScoreAdjustmentUseCase.removeReviewScoreAdjustment(adjustment.id!!)
        assertFalse(reviewScoreAdjustmentRepository.findById(adjustment.id!!).isPresent)
    }

    @Test
    fun `존재하지 않는 점수 가감을 삭제하면 예외가 발생한다`() {
        assertFailsWith<ReviewScoreAdjustmentNotFoundException> {
            removeReviewScoreAdjustmentUseCase.removeReviewScoreAdjustment(-1L)
        }
    }
}
