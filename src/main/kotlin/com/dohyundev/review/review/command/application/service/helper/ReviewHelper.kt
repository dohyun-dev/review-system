package com.dohyundev.review.review.command.application.service.helper

import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.exception.ReviewNotFoundException
import com.dohyundev.review.review.command.application.port.out.ReviewRepository

object ReviewHelper {
    fun findById(reviewId: Long, reviewRepository: ReviewRepository): Review {
        return reviewRepository.findById(reviewId).orElseThrow { ReviewNotFoundException() }
    }
}
