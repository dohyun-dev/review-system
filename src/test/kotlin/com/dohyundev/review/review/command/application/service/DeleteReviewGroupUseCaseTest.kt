package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotFoundException
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.application.port.`in`.DeleteReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents
import org.springframework.transaction.annotation.Transactional
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

@SpringBootTest
@Transactional
@RecordApplicationEvents
class DeleteReviewGroupUseCaseTest {

    @Autowired lateinit var deleteReviewGroupUseCase: DeleteReviewGroupUseCase
    @Autowired lateinit var reviewGroupRepository: ReviewGroupRepository
    @Autowired lateinit var applicationEvents: ApplicationEvents

    private lateinit var reviewGroup: ReviewGroup

    @BeforeTest
    fun setUp() {
        reviewGroup = reviewGroupRepository.save(ReviewGroupFixture.reviewGroup())
    }

    @Test
    fun `리뷰 그룹을 삭제한다`() {
        // when
        deleteReviewGroupUseCase.delete(reviewGroup.id!!)

        // then
        assertFalse(reviewGroupRepository.existsById(reviewGroup.id!!))
    }

    @Test
    fun `존재하지 않는 그룹을 삭제하면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<ReviewGroupNotFoundException> {
            deleteReviewGroupUseCase.delete(-1L)
        }
    }
}
