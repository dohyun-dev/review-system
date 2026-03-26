package com.dohyundev.review.review.group.domain

import com.dohyundev.review.review.group.fixture.ReviewGroupFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReviewSectionTest {

    @Test
    fun `create - 이름으로 섹션을 생성한다`() {
        val section = ReviewSection.create(name = "직무 역량")

        assertEquals("직무 역량", section.name)
        assertTrue(section.items.isEmpty())
    }

    @Test
    fun `create - 이름이 공백이면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            ReviewSection.create(name = " ")
        }
    }

    @Test
    fun `addItem - 항목을 추가하면 sortOrder가 자동 설정된다`() {
        val section = ReviewGroupFixture.createSection()
        val item = ReviewGroupFixture.createTextItem()

        section.addItem(item)

        assertEquals(1, section.items.size)
        assertEquals(1, item.sortOrder)
    }

    @Test
    fun `addItem - 다형성으로 여러 타입의 항목을 추가한다`() {
        val section = ReviewGroupFixture.createSection()
        val textItem = ReviewGroupFixture.createTextItem()
        val textareaItem = ReviewGroupFixture.createTextareaItem()
        val scoreItem = ReviewGroupFixture.createScoreChoiceItem()

        section.addItem(textItem)
        section.addItem(textareaItem)
        section.addItem(scoreItem)

        assertEquals(3, section.items.size)
        assertEquals(1, textItem.sortOrder)
        assertEquals(2, textareaItem.sortOrder)
        assertEquals(3, scoreItem.sortOrder)
        assertTrue(section.items[0] is TextReviewItem)
        assertTrue(section.items[1] is TextareaReviewItem)
        assertTrue(section.items[2] is ScoreChoiceReviewItem)
    }

    @Test
    fun `removeItem - 항목을 제거한다`() {
        val section = ReviewGroupFixture.createSection()
        val item = ReviewGroupFixture.createTextItem()
        section.addItem(item)

        section.removeItem(item)

        assertTrue(section.items.isEmpty())
    }
}
