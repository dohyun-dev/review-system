package com.dohyundev.review.review.command.domain.review.service

import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.command.ReviewItemCommand

interface ReviewItemFactory {
    fun supports(command: ReviewItemCommand): Boolean
    fun create(command: ReviewItemCommand): ReviewItem
}