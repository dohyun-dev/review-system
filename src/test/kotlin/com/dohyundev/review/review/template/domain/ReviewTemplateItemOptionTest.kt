package com.dohyundev.review.review.template.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReviewTemplateItemOptionTest {

    @Test
    fun `create - 내용과 점수로 점수 선택지를 생성한다`() {
        val option = ScoreChoiceItemOptionTemplate.create(content = "매우 우수", score = 5.0)

        assertEquals("매우 우수", option.content)
        assertEquals(5.0, option.score)
    }

    @Test
    fun `create - 음수 점수로 생성된다`() {
        val option = ScoreChoiceItemOptionTemplate.create(content = "감점 항목", score = -2.5)

        assertEquals(-2.5, option.score)
    }

    @Test
    fun `create - 내용이 공백이면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            ScoreChoiceItemOptionTemplate.create(content = " ", score = 5.0)
        }
    }

    @Test
    fun `ScoreChoiceItemOptionTemplate - score가 없는 경우 null이다`() {
        val option = ScoreChoiceItemOptionTemplate(content = "선택지")

        assertNull(option.score)
    }
}
