package com.dohyundev.review.review.template.domain

import com.dohyundev.review.review.template.fixture.ReviewTemplateFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReviewTemplateSectionTest {

    @Test
    fun `create - 이름으로 섹션을 생성한다`() {
        val section = ReviewTemplateSection.create(name = "직무 역량")

        assertEquals("직무 역량", section.name)
        assertTrue(section.items.isEmpty())
    }

    @Test
    fun `create - 이름이 공백이면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            ReviewTemplateSection.create(name = " ")
        }
    }

    @Test
    fun `addItem - 항목을 추가하면 sortOrder가 자동 설정된다`() {
        val section = ReviewTemplateFixture.createSection()
        val item = ReviewTemplateFixture.createScoreChoiceItem()

        section.addItem(item)

        assertEquals(1, section.items.size)
        assertEquals(1, item.sortOrder)
    }

    @Test
    fun `addItem - 다형성으로 여러 타입의 항목을 추가한다`() {
        val section = ReviewTemplateFixture.createSection()
        val scoreItem = ReviewTemplateFixture.createScoreChoiceItem()
        val textItem = ReviewTemplateFixture.createTextItem()
        val textareaItem = ReviewTemplateFixture.createTextareaItem()

        section.addItem(scoreItem)
        section.addItem(textItem)
        section.addItem(textareaItem)

        assertEquals(3, section.items.size)
        assertEquals(1, scoreItem.sortOrder)
        assertEquals(2, textItem.sortOrder)
        assertEquals(3, textareaItem.sortOrder)
        assertTrue(section.items[0] is ScoreChoiceItemTemplate)
        assertTrue(section.items[1] is TextItemTemplate)
        assertTrue(section.items[2] is TextareaItemTemplate)
    }

    @Test
    fun `removeItem - 항목을 제거한다`() {
        val section = ReviewTemplateFixture.createSection()
        val item = ReviewTemplateFixture.createScoreChoiceItem()
        section.addItem(item)

        section.removeItem(item)

        assertTrue(section.items.isEmpty())
    }
}
