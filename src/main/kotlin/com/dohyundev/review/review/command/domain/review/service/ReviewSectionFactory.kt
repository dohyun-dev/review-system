package com.dohyundev.review.review.command.domain.review.service

import com.dohyundev.review.review.command.domain.review.ReviewSection
import com.dohyundev.review.review.command.domain.review.command.ReviewSectionCommand

interface ReviewSectionFactory {
    fun createAll(commands: List<ReviewSectionCommand>): List<ReviewSection>
}