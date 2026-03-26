package com.dohyundev.review.review.template.application.port.`in`

import com.dohyundev.review.review.template.application.port.dto.ReviewTemplateItemCommand
import com.dohyundev.review.review.template.application.port.dto.ReviewTemplateSectionCommand
import com.dohyundev.review.review.template.application.port.dto.UpdateReviewTemplateFormCommand
import com.dohyundev.review.review.template.application.port.out.ReviewTemplateRepository
import com.dohyundev.review.review.template.application.port.service.ReviewTemplateCommandService
import com.dohyundev.review.review.template.domain.exception.ReviewTemplateNotFoundException
import com.dohyundev.review.review.template.fixture.ReviewTemplateFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UpdateFormReviewTemplateUseCaseTest {

    @Autowired
    lateinit var reviewTemplateCommandService: ReviewTemplateCommandService

    @Autowired
    lateinit var reviewTemplateRepository: ReviewTemplateRepository

    @Test
    fun `템플릿 전체 내용을 수정하고 DB에서 조회할 수 있다`() {
        val template = reviewTemplateRepository.save(ReviewTemplateFixture.createTemplate())

        val command = UpdateReviewTemplateFormCommand(
            sections = listOf(
                ReviewTemplateSectionCommand(
                    name = "새 섹션",
                    items = listOf(
                        ReviewTemplateItemCommand.Text(question = "새 질문"),
                    ),
                ),
            ),
        )

        reviewTemplateCommandService.updateForm(template.id!!, command)

        val found = reviewTemplateRepository.findById(template.id!!).orElseThrow()
        assertEquals(1, found.sections.size)
        assertEquals("새 섹션", found.sections.first().name)
    }

    @Test
    fun `존재하지 않는 템플릿 수정 시 예외가 발생한다`() {
        val command = UpdateReviewTemplateFormCommand(sections = emptyList())

        assertThrows<ReviewTemplateNotFoundException> {
            reviewTemplateCommandService.updateForm(999999L, command)
        }
    }
}
