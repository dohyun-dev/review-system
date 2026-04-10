package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.common.DateRange
import com.dohyundev.review.review.command.domain.group.ReviewGroupStatus
import com.dohyundev.review.review.command.application.port.`in`.CreateReviewGroupUseCase
import com.dohyundev.review.review.command.fixture.ReviewGroupCommandFixture
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@Transactional
class CreateReviewGroupUseCaseTest {

    @Autowired lateinit var createReviewGroupUseCase: CreateReviewGroupUseCase
    @Autowired lateinit var reviewGroupRepository: ReviewGroupRepository

    @Test
    fun `리뷰 그룹을 생성한다`() {
        // when
        createReviewGroupUseCase.create(ReviewGroupCommandFixture.createCommand())

        // then
        val saved = reviewGroupRepository.findAll().last()
        assertEquals("리뷰 그룹", saved.name)
        assertEquals(ReviewGroupStatus.PREPARING, saved.status)
    }

    @Test
    fun `설명, 기간, 대상 기간을 포함하여 생성한다`() {
        // given
        val period = DateRange(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31))
        val targetPeriod = DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))

        // when
        val id = createReviewGroupUseCase.create(
            ReviewGroupCommandFixture.createCommand(
                name = "2025 상반기",
                description = "상반기 리뷰",
                period = period,
                targetPeriod = targetPeriod,
            )
        )

        // then
        val saved = reviewGroupRepository.findById(id).get()
        assertEquals("상반기 리뷰", saved.description)
        assertEquals(period, saved.period)
        assertEquals(targetPeriod, saved.targetPeriod)
    }
}
