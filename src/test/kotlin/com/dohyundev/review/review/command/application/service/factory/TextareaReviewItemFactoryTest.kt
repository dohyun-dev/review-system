package com.dohyundev.review.review.command.application.service.factory

import com.dohyundev.review.review.command.domain.review.TextareaReviewItem
import com.dohyundev.review.review.command.domain.review.command.ScoreChoiceReviewItemCommand
import com.dohyundev.review.review.command.domain.review.command.TextareaReviewItemCommand
import com.dohyundev.review.review.command.application.port.`in`.command.ScoreChoiceOptionCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SpringBootTest
class TextareaReviewItemFactoryTest {

    @Autowired lateinit var factory: TextareaReviewItemFactory

    @Test
    fun `TextareaReviewItemCommand를 지원한다`() {
        val command = TextareaReviewItemCommand(question = "질문")
        assertTrue(factory.supports(command))
    }

    @Test
    fun `다른 커맨드는 지원하지 않는다`() {
        val command = ScoreChoiceReviewItemCommand(
            question = "질문",
            options = listOf(ScoreChoiceOptionCommand(label = "옵션1", score = 5.0)),
        )
        assertFalse(factory.supports(command))
    }

    @Test
    fun `TextareaReviewItem을 생성한다`() {
        val command = TextareaReviewItemCommand(
            question = "질문",
            description = "설명",
            isRequired = true,
            placeholder = "입력해주세요",
            maxLength = 500,
        )

        val item = factory.create(command)

        assertIs<TextareaReviewItem>(item)
        assertEquals("질문", item.question)
        assertEquals("설명", item.description)
        assertTrue(item.isRequired)
        assertEquals(0, item.sortOrder)
        assertEquals("입력해주세요", item.placeholder)
        assertEquals(500, item.maxLength)
    }

    @Test
    fun `placeholder와 maxLength가 없어도 생성된다`() {
        val command = TextareaReviewItemCommand(question = "질문")

        val item = factory.create(command) as TextareaReviewItem

        assertNull(item.placeholder)
        assertNull(item.maxLength)
    }
}
