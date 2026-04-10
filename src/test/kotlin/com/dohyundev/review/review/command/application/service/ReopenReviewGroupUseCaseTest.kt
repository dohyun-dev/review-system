package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.group.ReviewGroupStatus
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotFoundException
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.fixture.ReviewFixture
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewTargetFixture
import com.dohyundev.review.review.command.fixture.ReviewerFixture
import com.dohyundev.review.review.command.application.port.`in`.ReopenReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.`in`.StartReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.`in`.CloseReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.port.out.ReviewRepository
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@SpringBootTest
@Transactional
class ReopenReviewGroupUseCaseTest {

    @Autowired lateinit var reopenReviewGroupUseCase: ReopenReviewGroupUseCase
    @Autowired lateinit var startReviewGroupUseCase: StartReviewGroupUseCase
    @Autowired lateinit var closeReviewGroupUseCase: CloseReviewGroupUseCase
    @Autowired lateinit var reviewGroupRepository: ReviewGroupRepository
    @Autowired lateinit var reviewRepository: ReviewRepository
    @Autowired lateinit var reviewTargetRepository: ReviewTargetRepository
    @Autowired lateinit var reviewerRepository: ReviewerRepository

    private lateinit var reviewGroup: ReviewGroup

    @BeforeTest
    fun setUp() {
        reviewGroup = reviewGroupRepository.saveAndFlush(ReviewGroupFixture.reviewGroup())
        val review = reviewRepository.saveAndFlush(
            ReviewFixture.review(reviewGroup = reviewGroup, type = ReviewType.DOWNWARD)
        )
        val target = reviewTargetRepository.saveAndFlush(
            ReviewTargetFixture.target(reviewGroup = reviewGroup, employeeId = 1L)
        )
        reviewerRepository.saveAndFlush(
            ReviewerFixture.reviewer(
                reviewGroup = reviewGroup,
                review = review,
                reviewTarget = target,
                employeeId = 2L,
            )
        )
    }

    @Test
    fun `마감된 그룹을 재개한다`() {
        startReviewGroupUseCase.start(reviewGroup.id!!)
        closeReviewGroupUseCase.close(reviewGroup.id!!)
        reopenReviewGroupUseCase.reopen(reviewGroup.id!!)
        assertEquals(ReviewGroupStatus.IN_PROGRESS, reviewGroupRepository.findById(reviewGroup.id!!).get().status)
    }

    @Test
    fun `마감 상태가 아닌 그룹을 재개하면 예외가 발생한다`() {
        assertFailsWith<IllegalStateException> {
            reopenReviewGroupUseCase.reopen(reviewGroup.id!!)
        }
    }

    @Test
    fun `존재하지 않는 그룹을 재개하면 예외가 발생한다`() {
        assertFailsWith<ReviewGroupNotFoundException> {
            reopenReviewGroupUseCase.reopen(-1L)
        }
    }
}
