package com.dohyundev.review.review.query.controller

import com.dohyundev.review.review.command.domain.reviewer.ReviewerNotFoundException
import com.dohyundev.review.review.query.model.ReviewerFormView
import com.dohyundev.review.review.query.repository.ReviewerFormQueryRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "리뷰어 - 폼 조회", description = "리뷰어 폼 조회 API")
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/reviewers")
class ReviewerQueryApiController(
    private val reviewerFormQueryRepository: ReviewerFormQueryRepository,
) {
    @Operation(summary = "리뷰어 폼 조회")
    @GetMapping("/{reviewerId}/form")
    fun getReviewerForm(
        @PathVariable reviewerId: Long,
    ): ResponseEntity<ReviewerFormView> =
        ResponseEntity.ok(
            reviewerFormQueryRepository.findForm(reviewerId)
                ?: throw ReviewerNotFoundException()
        )
}
