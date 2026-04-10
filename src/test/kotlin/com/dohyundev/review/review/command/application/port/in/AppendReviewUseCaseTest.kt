package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.AppendReviewUseCase
import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.review.exception.DuplicateReviewTypeException
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotFoundException
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.fixture.ReviewCommandFixture
import com.dohyundev.review.review.command.fixture.ReviewFixture
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewTargetFixture
import com.dohyundev.review.review.command.fixture.ReviewerFixture
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
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
class AppendReviewUseCaseTest {

    @Autowired lateinit var appendReviewUseCase: AppendReviewUseCase
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
    }

    @Test
    fun `리뷰를 생성한다`() {
        val reviewId = appendReviewUseCase.appendReview(ReviewCommandFixture.createCommand(reviewGroupId = reviewGroup.id!!))
        assertTrue(reviewRepository.findById(reviewId).isPresent)
        assertEquals(ReviewType.SELF, reviewRepository.findById(reviewId).get().type)
    }

    @Test
    fun `중복된 타입으로 생성하면 예외가 발생한다`() {
        appendReviewUseCase.appendReview(ReviewCommandFixture.createCommand(reviewGroupId = reviewGroup.id!!))
        assertFailsWith<DuplicateReviewTypeException> {
            appendReviewUseCase.appendReview(ReviewCommandFixture.createCommand(reviewGroupId = reviewGroup.id!!))
        }
    }

    @Test
    fun `존재하지 않는 리뷰 그룹에 리뷰를 생성하면 예외가 발생한다`() {
        assertFailsWith<ReviewGroupNotFoundException> {
            appendReviewUseCase.appendReview(ReviewCommandFixture.createCommand(reviewGroupId = -1L))
        }
    }

    @Test
    fun `준비 중이 아닌 리뷰 그룹에 리뷰를 생성하면 예외가 발생한다`() {
        reviewGroup.start { }
        reviewGroupRepository.save(reviewGroup)
        assertFailsWith<IllegalStateException> {
            appendReviewUseCase.appendReview(ReviewCommandFixture.createCommand(reviewGroupId = reviewGroup.id!!))
        }
    }
}
