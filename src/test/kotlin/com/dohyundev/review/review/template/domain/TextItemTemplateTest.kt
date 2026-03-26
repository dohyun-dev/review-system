package com.dohyundev.review.review.template.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TextItemTemplateTest {

    @Test
    fun `create - 질문으로 주관식 항목을 생성한다`() {
        val item = TextItemTemplate.create(question = "잘한 점을 작성해주세요")

        assertEquals("잘한 점을 작성해주세요", item.question)
    }

    @Test
    fun `create - 질문이 공백이면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            TextItemTemplate.create(question = " ")
        }
    }
}
