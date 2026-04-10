package com.dohyundev.review.review.command.domain.review

import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItem
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItemOption
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ScoreChoiceReviewItemTest {

    @Test
    fun `점수 선택 아이템을 생성한다`() {
        // given
        val options = mutableListOf(
            ScoreChoiceReviewItemOption(label = "매우 좋음", score = 5.0, sortOrder = 1),
            ScoreChoiceReviewItemOption(label = "좋음", score = 4.0, sortOrder = 2),
        )

        // when
        val item = ScoreChoiceReviewItem(
            question = "질문",
            description = "설명",
            isRequired = true,
            sortOrder = 1,
            options = options,
        )

        // then
        assertEquals("질문", item.question)
        assertEquals("설명", item.description)
        assertEquals(true, item.isRequired)
        assertEquals(1, item.sortOrder)
        assertEquals(2, item.options.size)
        assertEquals(5.0, (item.options[0] as ScoreChoiceReviewItemOption).score)
        assertEquals("매우 좋음", item.options[0].label)
        assertEquals(4.0, (item.options[1] as ScoreChoiceReviewItemOption).score)
        assertEquals("좋음", item.options[1].label)
    }

    @Test
    fun `선택지가 없으면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            ScoreChoiceReviewItem(question = "질문", sortOrder = 1, options = mutableListOf())
        }
    }

    @Test
    fun `질문이 비어있으면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            ScoreChoiceReviewItem(question = "  ", sortOrder = 1)
        }
    }

    @Test
    fun `질문이 50자 이상이면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            ScoreChoiceReviewItem(question = "a".repeat(50), sortOrder = 1)
        }
    }

    @Test
    fun `설명이 100자 이상이면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            ScoreChoiceReviewItem(question = "질문", description = "a".repeat(100), sortOrder = 1)
        }
    }
}
