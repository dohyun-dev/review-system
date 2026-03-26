package com.dohyundev.review.review.group.domain

import com.dohyundev.review.review.group.fixture.ReviewGroupFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ScoreChoiceReviewItemTest {

    @Test
    fun `create - 질문으로 점수선택형 항목을 생성한다`() {
        val item = ScoreChoiceReviewItem.create(question = "업무 성과는 어떠했나요?")

        assertEquals("업무 성과는 어떠했나요?", item.question)
        assertTrue(item.options.isEmpty())
    }

    @Test
    fun `create - 질문이 공백이면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            ScoreChoiceReviewItem.create(question = " ")
        }
    }

    @Test
    fun `addOption - 선택지를 추가하면 sortOrder가 자동 설정된다`() {
        val item = ReviewGroupFixture.createScoreChoiceItem()
        val option = ReviewGroupFixture.createItemOption()

        item.addOption(option)

        assertEquals(1, item.options.size)
        assertEquals(1, option.sortOrder)
    }

    @Test
    fun `addOption - 여러 선택지를 순서대로 추가한다`() {
        val item = ReviewGroupFixture.createScoreChoiceItem()
        val option1 = ReviewGroupFixture.createItemOption(content = "매우 우수", score = 5.0)
        val option2 = ReviewGroupFixture.createItemOption(content = "우수", score = 4.0)

        item.addOption(option1)
        item.addOption(option2)

        assertEquals(1, option1.sortOrder)
        assertEquals(2, option2.sortOrder)
    }
}
