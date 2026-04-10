package com.dohyundev.review.review.command.domain.review

import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewItemFixture
import com.dohyundev.review.review.command.fixture.ReviewSectionFixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ReviewTest {

    @Test
    fun `리뷰를 생성한다`() {
        // when
        val review = Review(
            reviewGroup = ReviewGroupFixture.reviewGroup(),
            type = ReviewType.SELF,
            sections = mutableListOf(ReviewSectionFixture.section()),
        )

        // then
        assertEquals(ReviewType.SELF, review.type)
    }

    @Test
    fun `섹션이 없으면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            Review(reviewGroup = ReviewGroupFixture.reviewGroup(), type = ReviewType.SELF, sections = mutableListOf())
        }
    }

    @Test
    fun `타입을 수정한다`() {
        // given
        val review = Review(
            reviewGroup = ReviewGroupFixture.reviewGroup(),
            type = ReviewType.SELF,
            sections = mutableListOf(ReviewSectionFixture.section()),
        )

        // when
        review.update(ReviewType.UPWARD, listOf(ReviewSectionFixture.section()))

        // then
        assertEquals(ReviewType.UPWARD, review.type)
    }

    @Test
    fun `섹션을 수정한다`() {
        // given
        val review = Review(
            reviewGroup = ReviewGroupFixture.reviewGroup(),
            type = ReviewType.SELF,
            sections = mutableListOf(ReviewSectionFixture.section()),
        )

        // when
        review.update(ReviewType.SELF, listOf(ReviewSectionFixture.section(title = "수정된 섹션")))

        // then
        assertEquals(1, review.sections.size)
        assertEquals("수정된 섹션", review.sections.first().title)
    }

    @Test
    fun `섹션을 빈 목록으로 수정하면 예외가 발생한다`() {
        // given
        val review = Review(
            reviewGroup = ReviewGroupFixture.reviewGroup(),
            type = ReviewType.SELF,
            sections = mutableListOf(ReviewSectionFixture.section()),
        )

        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            review.update(ReviewType.SELF, emptyList())
        }
    }

    @Test
    fun `섹션 ID로 섹션을 찾는다`() {
        // given
        val section = ReviewSectionFixture.section().apply { id = 1L }
        val review = Review(
            reviewGroup = ReviewGroupFixture.reviewGroup(),
            type = ReviewType.SELF,
            sections = mutableListOf(section),
        )

        // when
        val found = review.findSectionById(1L)

        // then
        assertNotNull(found)
        assertEquals(section, found)
    }

    @Test
    fun `존재하지 않는 섹션 ID로 찾으면 null을 반환한다`() {
        // given
        val review = Review(
            reviewGroup = ReviewGroupFixture.reviewGroup(),
            type = ReviewType.SELF,
            sections = mutableListOf(ReviewSectionFixture.section().apply { id = 1L }),
        )

        // when
        val found = review.findSectionById(999L)

        // then
        assertNull(found)
    }

    @Test
    fun `아이템 ID로 아이템을 찾는다`() {
        // given
        val section = ReviewSectionFixture.section(
            items = mutableListOf(ReviewItemFixture.textItem(id = 10L))
        ).apply {
            id = 1L
        }
        val review = Review(
            reviewGroup = ReviewGroupFixture.reviewGroup(),
            type = ReviewType.SELF,
            sections = mutableListOf(section),
        )

        // when
        val found = review.findItemById(10L)

        // then
        assertNotNull(found)
        assertEquals(10L, found.id)
    }

    @Test
    fun `존재하지 않는 아이템 ID로 찾으면 null을 반환한다`() {
        // given
        val review = Review(
            reviewGroup = ReviewGroupFixture.reviewGroup(),
            type = ReviewType.SELF,
            sections = mutableListOf(ReviewSectionFixture.section().apply { id = 1L }),
        )

        // when
        val found = review.findItemById(999L)

        // then
        assertNull(found)
    }
}
