package com.dohyundev.review.review.command.domain.review.service

import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.command.CreateReviewCommand

interface ReviewFactory {
    fun create(command: CreateReviewCommand): Review
}
