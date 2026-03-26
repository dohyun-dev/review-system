package com.dohyundev.review.review.template.domain

import com.dohyundev.review.review.template.fixture.ReviewTemplateFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReviewTemplateTest {

    @Test
    fun `create - 이름과 순서로 리뷰 템플릿을 생성한다`() {
        val template = ReviewTemplate.create(name = "성과 리뷰", sortOrder = 1)

        assertEquals("성과 리뷰", template.name)
        assertEquals(1, template.sortOrder)
        assertTrue(template.sections.isEmpty())
    }

    @Test
    fun `create - 설명과 함께 생성한다`() {
        val template = ReviewTemplate.create(name = "성과 리뷰", sortOrder = 1, description = "연간 성과 평가용 템플릿")

        assertEquals("성과 리뷰", template.name)
        assertEquals("연간 성과 평가용 템플릿", template.description)
    }

    @Test
    fun `create - 이름이 공백이면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            ReviewTemplate.create(name = " ", sortOrder = 1)
        }
    }

    @Test
    fun `addSection - 섹션을 추가하면 sortOrder가 자동 설정된다`() {
        val template = ReviewTemplateFixture.createTemplate()
        val section = ReviewTemplateFixture.createSection()

        template.addSection(section)

        assertEquals(1, template.sections.size)
        assertEquals(1, section.sortOrder)
    }

    @Test
    fun `addSection - 여러 섹션을 순서대로 추가한다`() {
        val template = ReviewTemplateFixture.createTemplate()
        val section1 = ReviewTemplateFixture.createSection(name = "직무 역량")
        val section2 = ReviewTemplateFixture.createSection(name = "리더십")

        template.addSection(section1)
        template.addSection(section2)

        assertEquals(2, template.sections.size)
        assertEquals(1, section1.sortOrder)
        assertEquals(2, section2.sortOrder)
    }

    @Test
    fun `update - 이름, 설명, 순서를 수정한다`() {
        val template = ReviewTemplateFixture.createTemplate()

        template.update(name = "역량 리뷰", description = "수정된 설명", sortOrder = 2)

        assertEquals("역량 리뷰", template.name)
        assertEquals("수정된 설명", template.description)
        assertEquals(2, template.sortOrder)
    }

    @Test
    fun `update - 이름이 공백이면 예외가 발생한다`() {
        val template = ReviewTemplateFixture.createTemplate()

        assertThrows<IllegalArgumentException> {
            template.update(name = " ", description = null, sortOrder = 1)
        }
    }

    @Test
    fun `updateForm - 섹션을 한 번에 교체한다`() {
        val template = ReviewTemplateFixture.createTemplateWithSections()
        val newSections = listOf(ReviewTemplateFixture.createSection(name = "협업 역량"))

        template.updateForm(sections = newSections)

        assertEquals(1, template.sections.size)
        assertEquals("협업 역량", template.sections.first().name)
    }

    @Test
    fun `updateForm - 섹션이 비어있으면 기존 섹션이 모두 제거된다`() {
        val template = ReviewTemplateFixture.createTemplateWithSections()

        template.updateForm(sections = emptyList())

        assertTrue(template.sections.isEmpty())
    }

    @Test
    fun `removeSection - 섹션을 제거한다`() {
        val template = ReviewTemplateFixture.createTemplate()
        val section = ReviewTemplateFixture.createSection()
        template.addSection(section)

        template.removeSection(section)

        assertTrue(template.sections.isEmpty())
    }
}
