package com.dohyundev.review.review.command.domain.group

fun interface ReviewGroupStartableChecker {
    fun checkStartable(reviewGroup: ReviewGroup)
}