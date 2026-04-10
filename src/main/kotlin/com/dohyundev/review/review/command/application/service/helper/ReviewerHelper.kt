package com.dohyundev.review.review.command.application.service.helper

import com.dohyundev.review.review.command.domain.reviewer.Reviewer
import com.dohyundev.review.review.command.domain.reviewer.ReviewerNotFoundException
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository

object ReviewerHelper {
    fun findById(reviewerId: Long, reviewerRepository: ReviewerRepository): Reviewer {
        return reviewerRepository.findById(reviewerId).orElseThrow { ReviewerNotFoundException() }
    }
}
