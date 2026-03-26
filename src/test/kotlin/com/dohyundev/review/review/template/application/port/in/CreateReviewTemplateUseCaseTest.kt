package com.dohyundev.review.review.template.application.port.`in`

import com.dohyundev.review.review.template.application.port.dto.CreateReviewTemplateCommand
import com.dohyundev.review.review.template.application.port.dto.ReviewTemplateItemCommand
import com.dohyundev.review.review.template.application.port.dto.ReviewTemplateSectionCommand
import com.dohyundev.review.review.template.application.port.dto.ScoreSelectOptionCommand
import com.dohyundev.review.review.template.application.port.out.ReviewTemplateRepository
import com.dohyundev.review.review.template.application.port.service.ReviewTemplateCommandService
import com.dohyundev.review.review.template.domain.ScoreChoiceItemTemplate
import com.dohyundev.review.review.template.domain.TextItemTemplate
import com.dohyundev.review.review.template.domain.TextareaItemTemplate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class CreateReviewTemplateUseCaseTest {

    @Autowired
    lateinit var reviewTemplateCommandService: ReviewTemplateCommandService

    @Autowired
    lateinit var reviewTemplateRepository: ReviewTemplateRepository

    @Test
    fun `템플릿을 생성하고 DB에서 조회할 수 있다`() {
        val command = CreateReviewTemplateCommand(name = "성과 리뷰")

        val saved = reviewTemplateCommandService.create(command)

        val found = reviewTemplateRepository.findById(saved.id!!).orElseThrow()
        assertEquals("성과 리뷰", found.name)
        assertTrue(found.sections.isEmpty())
    }

    @Test
    fun `섹션과 항목을 포함한 템플릿을 생성한다`() {
        val command = CreateReviewTemplateCommand(
            name = "역량 리뷰",
            description = "연간 역량 평가",
            sections = listOf(
                ReviewTemplateSectionCommand(
                    name = "직무 역량",
                    items = listOf(
                        ReviewTemplateItemCommand.ScoreChoice(
                            question = "업무 성과는?",
                            options = listOf(
                                ScoreSelectOptionCommand(content = "매우 우수", score = 5.0),
                                ScoreSelectOptionCommand(content = "우수", score = 4.0),
                            ),
                        ),
                        ReviewTemplateItemCommand.Text(
                            question = "잘한 점을 작성해주세요",
                        ),
                        ReviewTemplateItemCommand.Textarea(
                            question = "개선할 점을 자세히 작성해주세요",
                        ),
                    ),
                ),
            ),
        )

        val saved = reviewTemplateCommandService.create(command)

        val found = reviewTemplateRepository.findById(saved.id!!).orElseThrow()
        assertEquals(1, found.sections.size)
        val section = found.sections.first()
        assertEquals("직무 역량", section.name)
        assertEquals(3, section.items.size)
        assertTrue(section.items[0] is ScoreChoiceItemTemplate)
        assertTrue(section.items[1] is TextItemTemplate)
        assertTrue(section.items[2] is TextareaItemTemplate)
    }
}
