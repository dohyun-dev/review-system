package com.dohyundev.review.review.command.fixture

import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.ReviewSection

object ReviewSectionFixture {

    fun section(
        title: String = "섹션",
        description: String? = null,
        sortOrder: Int = 1,
        items: MutableList<ReviewItem> = mutableListOf(ReviewItemFixture.textItem()),
    ) = ReviewSection(
        title = title,
        description = description,
        sortOrder = sortOrder,
        items = items,
    )
}
