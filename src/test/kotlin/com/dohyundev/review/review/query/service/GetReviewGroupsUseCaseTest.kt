package com.dohyundev.review.review.query.service

import com.dohyundev.review.common.DateRange
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.query.repository.ReviewGroupInfoQueryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SpringBootTest
@Transactional
class GetReviewGroupsUseCaseTest {

    @Autowired lateinit var reviewGroupInfoQueryRepository: ReviewGroupInfoQueryRepository
    @Autowired lateinit var reviewGroupRepository: ReviewGroupRepository

    @Test
    fun `리뷰 그룹 목록을 페이징으로 조회한다`() {
        // given
        reviewGroupRepository.save(ReviewGroupFixture.reviewGroup(name = "그룹 A"))
        reviewGroupRepository.save(ReviewGroupFixture.reviewGroup(name = "그룹 B"))

        // when
        val page = reviewGroupInfoQueryRepository.findAll(PageRequest.of(0, 10))

        // then
        assertEquals(2, page.totalElements)
        val names = page.content.map { it.name }
        assert(names.containsAll(listOf("그룹 A", "그룹 B")))
    }

    @Test
    fun `리뷰 그룹 요약 필드가 올바르게 매핑된다`() {
        // given
        val period = DateRange(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31))
        val targetPeriod = DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))
        reviewGroupRepository.save(
            ReviewGroupFixture.reviewGroup(
                name = "2025 리뷰",
                description = "연간 리뷰",
                period = period,
                targetPeriod = targetPeriod,
            )
        )

        // when
        val page = reviewGroupInfoQueryRepository.findAll(PageRequest.of(0, 10))

        // then
        val summary = page.content.first()
        assertEquals("2025 리뷰", summary.name)
        assertEquals("연간 리뷰", summary.description)
        assertEquals(LocalDate.of(2025, 1, 1), summary.periodFrom)
        assertEquals(LocalDate.of(2025, 12, 31), summary.periodTo)
        assertEquals(LocalDate.of(2024, 1, 1), summary.targetPeriodFrom)
        assertEquals(LocalDate.of(2024, 12, 31), summary.targetPeriodTo)
        assertEquals("준비중", summary.statusDisplayName)
    }

    @Test
    fun `기간이 없는 경우 null로 반환된다`() {
        // given
        reviewGroupRepository.save(ReviewGroupFixture.reviewGroup(name = "기간 없는 그룹"))

        // when
        val page = reviewGroupInfoQueryRepository.findAll(PageRequest.of(0, 10))

        // then
        val summary = page.content.first()
        assertNull(summary.periodFrom)
        assertNull(summary.periodTo)
        assertNull(summary.targetPeriodFrom)
        assertNull(summary.targetPeriodTo)
    }

    @Test
    fun `페이지 크기대로 잘라서 반환한다`() {
        // given
        repeat(5) { i ->
            reviewGroupRepository.save(ReviewGroupFixture.reviewGroup(name = "그룹 $i"))
        }

        // when
        val page = reviewGroupInfoQueryRepository.findAll(PageRequest.of(0, 3))

        // then
        assertEquals(5, page.totalElements)
        assertEquals(3, page.content.size)
    }
}
