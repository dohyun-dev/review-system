package com.dohyundev.review.review.command.application.port.`in`

interface RemoveReviewerUseCase {
    fun removeReviewer(reviewGroupId: Long, reviewerId: Long)
}