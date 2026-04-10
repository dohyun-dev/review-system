package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.review.command.fixture.ReviewFixture
import com.dohyundev.review.review.command.application.service.ReviewCommandService
import com.dohyundev.review.review.command.domain.review.service.ReviewFactory
import com.dohyundev.review.review.command.domain.review.service.ReviewSectionFactory
import com.dohyundev.review.review.command.application.port.out.ReviewRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Optional
import kotlin.test.Test

class ReviewCommandServiceTest {

    private val reviewRepository = mockk<ReviewRepository>(relaxed = true)
    private val reviewerRepository = mockk<ReviewerRepository>(relaxed = true)
    private val reviewFactory = mockk<ReviewFactory>(relaxed = true)
    private val reviewSectionFactory = mockk<ReviewSectionFactory>(relaxed = true)

    private val service = ReviewCommandService(
        reviewRepository = reviewRepository,
        reviewerRepository = reviewerRepository,
        reviewFactory = reviewFactory,
        reviewSectionFactory = reviewSectionFactory,
    )

    @Test
    fun `리뷰 삭제 시 reviewRepository에서 삭제된다`() {
        val review = ReviewFixture.review(id = 1L)
        every { reviewRepository.findById(1L) } returns Optional.of(review)

        service.removeReview(1L)

        verify { reviewRepository.deleteById(1L) }
    }
}
