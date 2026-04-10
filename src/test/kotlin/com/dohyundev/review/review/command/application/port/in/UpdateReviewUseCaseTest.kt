package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.AppendReviewUseCase
import com.dohyundev.review.review.command.application.port.`in`.UpdateReviewUseCase
import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.review.exception.DuplicateReviewTypeException
import com.dohyundev.review.review.command.domain.review.exception.ReviewNotFoundException
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

@SpringBootTest
@Transactional
class UpdateReviewUseCaseTest {

    @Autowired lateinit var appendReviewUseCase: AppendReviewUseCase
    @Autowired lateinit var updateReviewUseCase: UpdateReviewUseCase
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

    private fun createReviewId(type: ReviewType = ReviewType.SELF): Long =
        appendReviewUseCase.appendReview(ReviewCommandFixture.createCommand(reviewGroupId = reviewGroup.id!!, type = type))

    @Test
    fun `리뷰 타입을 수정한다`() {
        val reviewId = createReviewId()
        updateReviewUseCase.updateReview(ReviewCommandFixture.updateCommand(reviewGroupId = reviewGroup.id!!, reviewId = reviewId, type = ReviewType.UPWARD))
        val updated = reviewRepository.findById(reviewId).get()
        assertEquals(ReviewType.UPWARD, updated.type)
    }

    @Test
    fun `존재하지 않는 리뷰를 수정하면 예외가 발생한다`() {
        assertFailsWith<ReviewNotFoundException> {
            updateReviewUseCase.updateReview(ReviewCommandFixture.updateCommand(reviewGroupId = reviewGroup.id!!, reviewId = -1L))
        }
    }

    @Test
    fun `중복된 타입으로 수정하면 예외가 발생한다`() {
        createReviewId(type = ReviewType.SELF)
        val reviewId = createReviewId(type = ReviewType.UPWARD)
        assertFailsWith<DuplicateReviewTypeException> {
            updateReviewUseCase.updateReview(ReviewCommandFixture.updateCommand(reviewGroupId = reviewGroup.id!!, reviewId = reviewId, type = ReviewType.SELF))
        }
    }

    @Test
    fun `준비 중이 아닌 리뷰 그룹의 리뷰를 수정하면 예외가 발생한다`() {
        val reviewId = createReviewId()
        reviewGroup.start { }
        reviewGroupRepository.save(reviewGroup)
        assertFailsWith<IllegalStateException> {
            updateReviewUseCase.updateReview(ReviewCommandFixture.updateCommand(reviewGroupId = reviewGroup.id!!, reviewId = reviewId, type = ReviewType.UPWARD))
        }
    }
}
