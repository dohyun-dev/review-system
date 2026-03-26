package com.dohyundev.review.review.template.application.port.`in`

import com.dohyundev.review.review.template.application.port.out.ReviewTemplateRepository
import com.dohyundev.review.review.template.application.service.ReviewTemplateCommandService
import com.dohyundev.review.review.template.domain.exception.ReviewTemplateNotFoundException
import com.dohyundev.review.review.template.fixture.ReviewTemplateFixture
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class DeleteReviewTemplateUseCaseTest {

    @Autowired
    lateinit var reviewTemplateCommandService: ReviewTemplateCommandService

    @Autowired
    lateinit var reviewTemplateRepository: ReviewTemplateRepository

    @Test
    fun `템플릿을 삭제하면 DB에서 조회되지 않는다`() {
        val template = reviewTemplateRepository.save(ReviewTemplateFixture.createTemplate())

        reviewTemplateCommandService.delete(template.id!!)

        assertFalse(reviewTemplateRepository.existsById(template.id!!))
    }

    @Test
    fun `존재하지 않는 템플릿 삭제 시 예외가 발생한다`() {
        assertThrows<ReviewTemplateNotFoundException> {
            reviewTemplateCommandService.delete(999999L)
        }
    }
}
