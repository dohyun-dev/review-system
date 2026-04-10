package com.dohyundev.review.review.query.model

data class ReviewerFormView(
    val reviewerInfo: ReviewerInfoView,
    val sections: List<ReviewerFormSectionView>,
)
