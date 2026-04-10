package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.employee.command.port.out.EmployeeRepository
import com.dohyundev.review.employee.command.domain.exception.EmployeeNotFoundException
import com.dohyundev.review.employee.fixture.EmployeeFixture
import com.dohyundev.review.review.command.application.port.`in`.AppendReviewerUseCase
import com.dohyundev.review.review.command.application.port.`in`.RemoveReviewerUseCase
import com.dohyundev.review.review.command.application.port.`in`.command.AppendReviewerCommand
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.port.out.ReviewRepository
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotFoundException
import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.domain.review.exception.ReviewNotFoundException
import com.dohyundev.review.review.command.domain.reviewer.DuplicateReviewerException
import com.dohyundev.review.review.command.domain.reviewer.ReviewerNotFoundException
import com.dohyundev.review.review.command.domain.target.ReviewTarget
import com.dohyundev.review.review.command.domain.target.ReviewTargetNotFoundException
import com.dohyundev.review.review.command.fixture.ReviewFixture
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewTargetFixture
import com.dohyundev.review.review.command.fixture.ReviewerFixture
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
class ReviewerCommandServiceTest {

    @Autowired lateinit var appendReviewerUseCase: AppendReviewerUseCase
    @Autowired lateinit var removeReviewerUseCase: RemoveReviewerUseCase
    @Autowired lateinit var reviewGroupRepository: ReviewGroupRepository
    @Autowired lateinit var reviewRepository: ReviewRepository
    @Autowired lateinit var reviewTargetRepository: ReviewTargetRepository
    @Autowired lateinit var reviewerRepository: ReviewerRepository
    @Autowired lateinit var employeeRepository: EmployeeRepository

    private lateinit var reviewGroup: ReviewGroup
    private lateinit var review: Review
    private lateinit var target: ReviewTarget
    private var employeeId: Long = 0L

    @BeforeTest
    fun setUp() {
        reviewGroup = reviewGroupRepository.saveAndFlush(ReviewGroupFixture.reviewGroup())
        review = reviewRepository.saveAndFlush(
            ReviewFixture.review(reviewGroup = reviewGroup, type = ReviewType.DOWNWARD)
        )
        target = reviewTargetRepository.saveAndFlush(
            ReviewTargetFixture.target(reviewGroup = reviewGroup, employeeId = 9999L)
        )
        employeeId = employeeRepository.saveAndFlush(EmployeeFixture.employee()).id!!
    }

    @Test
    fun `리뷰어를 추가한다`() {
        val reviewerId = appendReviewerUseCase.appendReviewer(
            AppendReviewerCommand(
                reviewGroupId = reviewGroup.id!!,
                reviewId = review.id!!,
                reviewTargetId = target.id!!,
                employeeId = employeeId,
            )
        )
        assertTrue(reviewerRepository.findById(reviewerId).isPresent)
    }

    @Test
    fun `존재하지 않는 리뷰 그룹에 리뷰어를 추가하면 예외가 발생한다`() {
        assertFailsWith<ReviewGroupNotFoundException> {
            appendReviewerUseCase.appendReviewer(
                AppendReviewerCommand(
                    reviewGroupId = -1L,
                    reviewId = review.id!!,
                    reviewTargetId = target.id!!,
                    employeeId = employeeId,
                )
            )
        }
    }

    @Test
    fun `존재하지 않는 리뷰에 리뷰어를 추가하면 예외가 발생한다`() {
        assertFailsWith<ReviewNotFoundException> {
            appendReviewerUseCase.appendReviewer(
                AppendReviewerCommand(
                    reviewGroupId = reviewGroup.id!!,
                    reviewId = -1L,
                    reviewTargetId = target.id!!,
                    employeeId = employeeId,
                )
            )
        }
    }

    @Test
    fun `존재하지 않는 리뷰 대상자에 리뷰어를 추가하면 예외가 발생한다`() {
        assertFailsWith<ReviewTargetNotFoundException> {
            appendReviewerUseCase.appendReviewer(
                AppendReviewerCommand(
                    reviewGroupId = reviewGroup.id!!,
                    reviewId = review.id!!,
                    reviewTargetId = -1L,
                    employeeId = employeeId,
                )
            )
        }
    }

    @Test
    fun `존재하지 않는 사원을 리뷰어로 추가하면 예외가 발생한다`() {
        assertFailsWith<EmployeeNotFoundException> {
            appendReviewerUseCase.appendReviewer(
                AppendReviewerCommand(
                    reviewGroupId = reviewGroup.id!!,
                    reviewId = review.id!!,
                    reviewTargetId = target.id!!,
                    employeeId = -1L,
                )
            )
        }
    }

    @Test
    fun `같은 조합으로 리뷰어를 중복 추가하면 예외가 발생한다`() {
        val command = AppendReviewerCommand(
            reviewGroupId = reviewGroup.id!!,
            reviewId = review.id!!,
            reviewTargetId = target.id!!,
            employeeId = employeeId,
        )
        appendReviewerUseCase.appendReviewer(command)
        assertFailsWith<DuplicateReviewerException> {
            appendReviewerUseCase.appendReviewer(command)
        }
    }

    @Test
    fun `리뷰어를 삭제한다`() {
        val reviewer = reviewerRepository.saveAndFlush(
            ReviewerFixture.reviewer(
                reviewGroup = reviewGroup,
                review = review,
                reviewTarget = target,
                employeeId = employeeId,
            )
        )
        removeReviewerUseCase.removeReviewer(reviewGroup.id!!, reviewer.id!!)
        assertFalse(reviewerRepository.findById(reviewer.id!!).isPresent)
    }

    @Test
    fun `존재하지 않는 리뷰어를 삭제하면 예외가 발생한다`() {
        assertFailsWith<ReviewerNotFoundException> {
            removeReviewerUseCase.removeReviewer(reviewGroup.id!!, -1L)
        }
    }
}
