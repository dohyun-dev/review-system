package com.dohyundev.review.review.group.application.port.`in`

import com.dohyundev.review.review.group.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.group.application.service.ReviewGroupCommandService
import com.dohyundev.review.review.group.domain.exception.ReviewGroupNotFoundException
import com.dohyundev.review.review.group.fixture.ReviewGroupFixture
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class DeleteReviewGroupUseCaseTest {

    @Autowired
    lateinit var reviewGroupCommandService: ReviewGroupCommandService

    @Autowired
    lateinit var reviewGroupRepository: ReviewGroupRepository

    @Test
    fun `리뷰 그룹을 삭제한다`() {
        val saved = reviewGroupRepository.save(ReviewGroupFixture.createReviewGroup())

        reviewGroupCommandService.delete(saved.id!!)

        assertFalse(reviewGroupRepository.existsById(saved.id!!))
    }

    @Test
    fun `존재하지 않는 리뷰 그룹 삭제 시 예외가 발생한다`() {
        assertThrows<ReviewGroupNotFoundException> {
            reviewGroupCommandService.delete(999999L)
        }
    }
}
