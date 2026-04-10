package com.dohyundev.review.review.command.domain.review

import com.dohyundev.review.review.command.domain.review.ReviewSection
import com.dohyundev.review.review.command.fixture.ReviewItemFixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ReviewSectionTest {

    @Test
    fun `섹션을 생성한다`() {
        // when
        val section = ReviewSection(
            title = "섹션",
            description = "설명",
            sortOrder = 1,
            items = mutableListOf(ReviewItemFixture.textItem()),
        )

        // then
        assertEquals("섹션", section.title)
        assertEquals("설명", section.description)
        assertEquals(1, section.sortOrder)
    }

    @Test
    fun `아이템이 없으면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            ReviewSection(title = "섹션", sortOrder = 1, items = mutableListOf())
        }
    }


    @Test
    fun `섹션명이 비어있으면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            ReviewSection(title = "  ", sortOrder = 1, items = mutableListOf(ReviewItemFixture.textItem()))
        }
    }

    @Test
    fun `섹션명이 50자 이상이면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            ReviewSection(title = "a".repeat(50), sortOrder = 1, items = mutableListOf(ReviewItemFixture.textItem()))
        }
    }

    @Test
    fun `설명이 100자 이상이면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            ReviewSection(
                title = "섹션",
                description = "a".repeat(100),
                sortOrder = 1,
                items = mutableListOf(ReviewItemFixture.textItem())
            )
        }
    }
}
