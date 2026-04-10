package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.group.ReviewGroupStatus
import com.dohyundev.review.review.command.domain.group.exception.InsufficientReviewCountException
import com.dohyundev.review.review.command.domain.group.exception.InsufficientReviewTargetCountException
import com.dohyundev.review.review.command.domain.group.exception.InsufficientReviewerCountException
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotFoundException
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.fixture.ReviewFixture
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewTargetFixture
import com.dohyundev.review.review.command.fixture.ReviewerFixture
import com.dohyundev.review.review.command.application.port.`in`.StartReviewGroupUseCase
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
class StartReviewGroupUseCaseTest {

    @Autowired lateinit var startReviewGroupUseCase: StartReviewGroupUseCase
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
    fun `리뷰 그룹을 시작한다`() {
        startReviewGroupUseCase.start(reviewGroup.id!!)
        assertEquals(ReviewGroupStatus.IN_PROGRESS, reviewGroupRepository.findById(reviewGroup.id!!).get().status)
    }

    @Test
    fun `준비 중이 아닌 그룹을 시작하면 예외가 발생한다`() {
        startReviewGroupUseCase.start(reviewGroup.id!!)
        assertFailsWith<IllegalStateException> { startReviewGroupUseCase.start(reviewGroup.id!!) }
    }

    @Test
    fun `존재하지 않는 그룹을 시작하면 예외가 발생한다`() {
        assertFailsWith<ReviewGroupNotFoundException> { startReviewGroupUseCase.start(-1L) }
    }

    @Test
    fun `리뷰가 없는 그룹을 시작하면 예외가 발생한다`() {
        val emptyGroup = reviewGroupRepository.saveAndFlush(ReviewGroupFixture.reviewGroup())
        assertFailsWith<InsufficientReviewCountException> { startReviewGroupUseCase.start(emptyGroup.id!!) }
    }

    @Test
    fun `리뷰 대상자가 없는 그룹을 시작하면 예외가 발생한다`() {
        val group = reviewGroupRepository.saveAndFlush(ReviewGroupFixture.reviewGroup())
        reviewRepository.saveAndFlush(ReviewFixture.review(reviewGroup = group, type = ReviewType.DOWNWARD))
        assertFailsWith<InsufficientReviewTargetCountException> { startReviewGroupUseCase.start(group.id!!) }
    }

    @Test
    fun `리뷰어가 없는 그룹을 시작하면 예외가 발생한다`() {
        val group = reviewGroupRepository.saveAndFlush(ReviewGroupFixture.reviewGroup())
        reviewRepository.saveAndFlush(ReviewFixture.review(reviewGroup = group, type = ReviewType.DOWNWARD))
        reviewTargetRepository.saveAndFlush(ReviewTargetFixture.target(reviewGroup = group, employeeId = 1L))
        assertFailsWith<InsufficientReviewerCountException> { startReviewGroupUseCase.start(group.id!!) }
    }
}
