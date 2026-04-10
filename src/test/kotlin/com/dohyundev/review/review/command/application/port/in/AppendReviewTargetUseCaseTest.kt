package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.employee.command.port.out.EmployeeRepository
import com.dohyundev.review.employee.command.domain.Employee
import com.dohyundev.review.employee.command.domain.exception.EmployeeNotFoundException
import com.dohyundev.review.employee.fixture.EmployeeFixture
import com.dohyundev.review.review.command.application.port.`in`.AppendReviewTargetUseCase
import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.target.DuplicateReviewTargetException
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotFoundException
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.fixture.ReviewFixture
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewTargetFixture
import com.dohyundev.review.review.command.fixture.ReviewerFixture
import com.dohyundev.review.review.command.application.port.`in`.command.CreateReviewTargetCommand
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.port.out.ReviewRepository
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
class AppendReviewTargetUseCaseTest {

    @Autowired lateinit var appendReviewTargetUseCase: AppendReviewTargetUseCase
    @Autowired lateinit var reviewGroupRepository: ReviewGroupRepository
    @Autowired lateinit var reviewRepository: ReviewRepository
    @Autowired lateinit var reviewTargetRepository: ReviewTargetRepository
    @Autowired lateinit var reviewerRepository: ReviewerRepository
    @Autowired lateinit var employeeRepository: EmployeeRepository

    private lateinit var reviewGroup: ReviewGroup
    private lateinit var employee: Employee

    @BeforeTest
    fun setUp() {
        reviewGroup = reviewGroupRepository.saveAndFlush(ReviewGroupFixture.reviewGroup())
        val review = reviewRepository.saveAndFlush(
            ReviewFixture.review(reviewGroup = reviewGroup, type = ReviewType.DOWNWARD)
        )
        val target = reviewTargetRepository.saveAndFlush(
            ReviewTargetFixture.target(reviewGroup = reviewGroup, employeeId = 9999L)
        )
        reviewerRepository.saveAndFlush(
            ReviewerFixture.reviewer(
                reviewGroup = reviewGroup,
                review = review,
                reviewTarget = target,
                employeeId = 9998L,
            )
        )
        employee = employeeRepository.save(EmployeeFixture.employee())
    }

    @Test
    fun `리뷰 대상자를 추가한다`() {
        val reviewTargetId = appendReviewTargetUseCase.appendReviewTarget(
            CreateReviewTargetCommand(reviewGroupId = reviewGroup.id!!, employeeId = employee.id!!)
        )
        assertTrue(reviewTargetRepository.findById(reviewTargetId).isPresent)
    }

    @Test
    fun `존재하지 않는 리뷰 그룹에 리뷰 대상자를 추가하면 예외가 발생한다`() {
        assertFailsWith<ReviewGroupNotFoundException> {
            appendReviewTargetUseCase.appendReviewTarget(CreateReviewTargetCommand(reviewGroupId = -1L, employeeId = employee.id!!))
        }
    }

    @Test
    fun `존재하지 않는 사원을 리뷰 대상자로 추가하면 예외가 발생한다`() {
        assertFailsWith<EmployeeNotFoundException> {
            appendReviewTargetUseCase.appendReviewTarget(CreateReviewTargetCommand(reviewGroupId = reviewGroup.id!!, employeeId = -1L))
        }
    }

    @Test
    fun `같은 사원을 중복으로 추가하면 예외가 발생한다`() {
        appendReviewTargetUseCase.appendReviewTarget(CreateReviewTargetCommand(reviewGroupId = reviewGroup.id!!, employeeId = employee.id!!))
        assertFailsWith<DuplicateReviewTargetException> {
            appendReviewTargetUseCase.appendReviewTarget(CreateReviewTargetCommand(reviewGroupId = reviewGroup.id!!, employeeId = employee.id!!))
        }
    }

    @Test
    fun `마감된 리뷰 그룹에 리뷰 대상자를 추가하면 예외가 발생한다`() {
        reviewGroup.start { }
        reviewGroup.close()
        reviewGroupRepository.saveAndFlush(reviewGroup)
        assertFailsWith<IllegalStateException> {
            appendReviewTargetUseCase.appendReviewTarget(CreateReviewTargetCommand(reviewGroupId = reviewGroup.id!!, employeeId = employee.id!!))
        }
    }
}
