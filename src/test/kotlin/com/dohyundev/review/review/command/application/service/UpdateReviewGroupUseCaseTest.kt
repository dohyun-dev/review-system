package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotFoundException
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.fixture.ReviewFixture
import com.dohyundev.review.review.command.fixture.ReviewGroupCommandFixture
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewTargetFixture
import com.dohyundev.review.review.command.fixture.ReviewerFixture
import com.dohyundev.review.review.command.application.port.`in`.UpdateReviewGroupUseCase
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
class UpdateReviewGroupUseCaseTest {

    @Autowired lateinit var updateReviewGroupUseCase: UpdateReviewGroupUseCase
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
    fun `이름과 설명을 수정한다`() {
        updateReviewGroupUseCase.update(
            ReviewGroupCommandFixture.updateCommand(reviewGroupId = reviewGroup.id!!, description = "수정된 설명")
        )

        val updated = reviewGroupRepository.findById(reviewGroup.id!!).get()
        assertEquals("수정된 이름", updated.name)
        assertEquals("수정된 설명", updated.description)
    }

    @Test
    fun `준비 중이 아닌 그룹을 수정하면 예외가 발생한다`() {
        reviewGroup.start { }
        reviewGroupRepository.save(reviewGroup)

        assertFailsWith<IllegalStateException> {
            updateReviewGroupUseCase.update(
                ReviewGroupCommandFixture.updateCommand(reviewGroupId = reviewGroup.id!!)
            )
        }
    }

    @Test
    fun `존재하지 않는 그룹을 수정하면 예외가 발생한다`() {
        assertFailsWith<ReviewGroupNotFoundException> {
            updateReviewGroupUseCase.update(
                ReviewGroupCommandFixture.updateCommand(reviewGroupId = -1L)
            )
        }
    }
}
