package com.dohyundev.review.review.command.application.service.factory

import com.dohyundev.review.review.command.application.port.`in`.command.ScoreChoiceOptionCommand
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItem
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItemOption
import com.dohyundev.review.review.command.domain.review.command.ScoreChoiceReviewItemCommand
import com.dohyundev.review.review.command.domain.review.command.TextareaReviewItemCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@SpringBootTest
class ScoreChoiceReviewItemFactoryTest {

    @Autowired lateinit var factory: ScoreChoiceReviewItemFactory

    @Test
    fun `ScoreChoiceReviewItemCommand를 지원한다`() {
        val command = ScoreChoiceReviewItemCommand(
            question = "질문",
            options = listOf(ScoreChoiceOptionCommand(label = "옵션1", score = 5.0)),
        )
        assertTrue(factory.supports(command))
    }

    @Test
    fun `다른 커맨드는 지원하지 않는다`() {
        val command = TextareaReviewItemCommand(question = "질문")
        assertFalse(factory.supports(command))
    }

    @Test
    fun `ScoreChoiceReviewItem을 생성한다`() {
        val command = ScoreChoiceReviewItemCommand(
            question = "질문",
            description = "설명",
            isRequired = true,
            options = listOf(
                ScoreChoiceOptionCommand(label = "레이블1", description = "옵션설명", score = 5.0),
                ScoreChoiceOptionCommand(label = "옵션2", score = 3.0),
            ),
        )

        val item = factory.create(command)

        assertIs<ScoreChoiceReviewItem>(item)
        assertEquals("질문", item.question)
        assertEquals("설명", item.description)
        assertTrue(item.isRequired)
        assertEquals(0, item.sortOrder)
        assertEquals(2, item.options.size)
        assertEquals(5.0, (item.options[0] as ScoreChoiceReviewItemOption).score)
        assertEquals(3.0, (item.options[1] as ScoreChoiceReviewItemOption).score)
    }
}
