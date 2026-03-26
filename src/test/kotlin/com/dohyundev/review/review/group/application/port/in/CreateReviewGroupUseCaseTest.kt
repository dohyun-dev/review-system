package com.dohyundev.review.review.group.application.port.`in`

import com.dohyundev.review.review.group.application.dto.CreateReviewGroupCommand
import com.dohyundev.review.review.group.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.group.application.service.ReviewGroupCommandService
import com.dohyundev.review.review.group.domain.ReviewGroupStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class CreateReviewGroupUseCaseTest {

    @Autowired
    lateinit var reviewGroupCommandService: ReviewGroupCommandService

    @Autowired
    lateinit var reviewGroupRepository: ReviewGroupRepository

    @Test
    fun `리뷰 그룹을 생성하고 DB에서 조회할 수 있다`() {
        val command = CreateReviewGroupCommand(name = "2026 상반기 리뷰", description = "상반기 성과 리뷰")

        val saved = reviewGroupCommandService.create(command)

        val found = reviewGroupRepository.findById(saved.id!!).orElseThrow()
        assertEquals("2026 상반기 리뷰", found.name)
        assertEquals("상반기 성과 리뷰", found.description)
        assertEquals(ReviewGroupStatus.PREPARING, found.status)
        assertTrue(found.reviews.isEmpty())
        assertTrue(found.targets.isEmpty())
        assertTrue(found.reviewers.isEmpty())
    }
}
