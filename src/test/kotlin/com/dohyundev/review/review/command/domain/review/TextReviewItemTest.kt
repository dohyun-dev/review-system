package com.dohyundev.review.review.command.domain.review

import com.dohyundev.review.review.command.domain.review.TextReviewItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TextReviewItemTest {

    @Test
    fun `주관식 항목을 생성한다`() {
        // when
        val item = TextReviewItem(
            question = "질문",
            description = "설명",
            isRequired = true,
            sortOrder = 1,
            placeholder = "입력하세요",
            maxLength = 100,
        )

        // then
        assertEquals("질문", item.question)
        assertEquals("설명", item.description)
        assertEquals(true, item.isRequired)
        assertEquals(1, item.sortOrder)
        assertEquals("입력하세요", item.placeholder)
        assertEquals(100, item.maxLength)
    }

    @Test
    fun `질문이 비어있으면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            TextReviewItem(question = "  ", sortOrder = 1)
        }
    }

    @Test
    fun `질문이 50자 이상이면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            TextReviewItem(question = "a".repeat(50), sortOrder = 1)
        }
    }

    @Test
    fun `설명이 100자 이상이면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            TextReviewItem(question = "질문", description = "a".repeat(100), sortOrder = 1)
        }
    }
}
