package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.employee.command.port.out.EmployeeRepository
import com.dohyundev.review.employee.command.domain.Employee
import com.dohyundev.review.employee.fixture.EmployeeFixture
import com.dohyundev.review.review.command.application.port.`in`.AppendReviewTargetUseCase
import com.dohyundev.review.review.command.application.port.`in`.RemoveReviewTargetUseCase
import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.target.ReviewTargetNotFoundException
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
import kotlin.test.assertFalse

@SpringBootTest
@Transactional
class RemoveReviewTargetUseCaseTest {

    @Autowired lateinit var appendReviewTargetUseCase: AppendReviewTargetUseCase
    @Autowired lateinit var removeReviewTargetUseCase: RemoveReviewTargetUseCase
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
    fun `리뷰 대상자를 삭제한다`() {
        val targetId = appendReviewTargetUseCase.appendReviewTarget(
            CreateReviewTargetCommand(reviewGroupId = reviewGroup.id!!, employeeId = employee.id!!)
        )
        removeReviewTargetUseCase.removeReviewTarget(targetId)
        assertFalse(reviewTargetRepository.findById(targetId).isPresent)
    }

    @Test
    fun `마감된 리뷰 그룹의 리뷰 대상자를 삭제하면 예외가 발생한다`() {
        val targetId = appendReviewTargetUseCase.appendReviewTarget(
            CreateReviewTargetCommand(reviewGroupId = reviewGroup.id!!, employeeId = employee.id!!)
        )
        reviewGroup.start { }
        reviewGroup.close()
        reviewGroupRepository.save(reviewGroup)
        assertFailsWith<IllegalStateException> { removeReviewTargetUseCase.removeReviewTarget(targetId) }
    }

    @Test
    fun `존재하지 않는 리뷰 대상자를 삭제하면 예외가 발생한다`() {
        assertFailsWith<ReviewTargetNotFoundException> {
            removeReviewTargetUseCase.removeReviewTarget(-1L)
        }
    }
}
