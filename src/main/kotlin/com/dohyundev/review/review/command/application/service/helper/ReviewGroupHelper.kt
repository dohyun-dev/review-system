package com.dohyundev.review.review.command.application.service.helper

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotFoundException
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository

object ReviewGroupHelper {
    fun findById(reviewGroupId: Long, reviewGroupRepository: ReviewGroupRepository): ReviewGroup =
        reviewGroupRepository.findById(reviewGroupId)
            .orElseThrow { ReviewGroupNotFoundException() }
}
