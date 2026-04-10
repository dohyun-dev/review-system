package com.dohyundev.review.review.command.application.service.helper

import com.dohyundev.review.review.command.domain.target.ReviewTarget
import com.dohyundev.review.review.command.domain.target.ReviewTargetNotFoundException
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository

object ReviewTargetHelper {
    fun findById(reviewTargetId: Long, reviewTargetRepository: ReviewTargetRepository): ReviewTarget {
        return reviewTargetRepository.findById(reviewTargetId).orElseThrow { ReviewTargetNotFoundException() }
    }
}
