package com.dohyundev.review.review.template.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TextareaItemTemplateTest {

    @Test
    fun `create - 질문으로 서술형 항목을 생성한다`() {
        val item = TextareaItemTemplate.create(question = "개선할 점을 자세히 작성해주세요")

        assertEquals("개선할 점을 자세히 작성해주세요", item.question)
    }

    @Test
    fun `create - 질문이 공백이면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            TextareaItemTemplate.create(question = " ")
        }
    }
}
