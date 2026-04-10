package com.dohyundev.review.review.query.controller

import com.dohyundev.review.review.command.domain.group.ReviewGroupStatus
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotFoundException
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotInProgressException
import com.dohyundev.review.review.query.controller.dto.ReviewGroupDetailResponse
import com.dohyundev.review.review.query.controller.dto.ReviewGroupProgressResponse
import com.dohyundev.review.review.query.controller.dto.ReviewGroupResultResponse
import com.dohyundev.review.review.query.model.ReviewDetailView
import com.dohyundev.review.review.query.model.ReviewGroupInfoView
import com.dohyundev.review.review.query.repository.ReviewGroupInfoQueryRepository
import com.dohyundev.review.review.query.repository.ReviewDetailQueryRepository
import com.dohyundev.review.review.query.repository.ReviewProgressSummaryQueryRepository
import com.dohyundev.review.review.query.repository.ReviewTargetResultQueryRepository
import com.dohyundev.review.review.query.repository.ReviewerProgressInfoQueryRepository
import com.dohyundev.review.review.query.repository.ReviewerInfoQueryRepository
import com.dohyundev.review.review.query.repository.ReviewSummaryQueryRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "관리자 - 리뷰 그룹 조회", description = "리뷰 그룹 목록 조회 API")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin/review-groups")
class AdminReviewGroupQueryApiController(
    private val reviewGroupInfoQueryRepository: ReviewGroupInfoQueryRepository,
    private val reviewSummaryQueryRepository: ReviewSummaryQueryRepository,
    private val reviewerInfoQueryRepository: ReviewerInfoQueryRepository,
    private val reviewProgressSummaryQueryRepository: ReviewProgressSummaryQueryRepository,
    private val reviewerProgressInfoQueryRepository: ReviewerProgressInfoQueryRepository,
    private val reviewDetailQueryRepository: ReviewDetailQueryRepository,
    private val reviewTargetResultQueryRepository: ReviewTargetResultQueryRepository,
) {
    @Operation(summary = "리뷰 그룹 목록 조회")
    @GetMapping
    fun getReviewGroups(
        @PageableDefault(size = 20) pageable: Pageable,
    ): ResponseEntity<Page<ReviewGroupInfoView>> =
        ResponseEntity.ok(reviewGroupInfoQueryRepository.findAll(pageable))

    @Operation(summary = "리뷰 그룹 상세 조회")
    @GetMapping("/{reviewGroupId}")
    fun getReviewGroupDetail(
        @PathVariable reviewGroupId: Long,
    ): ResponseEntity<ReviewGroupDetailResponse> {
        val info = reviewGroupInfoQueryRepository.findInfo(reviewGroupId)
            ?: throw ReviewGroupNotFoundException()
        return ResponseEntity.ok(
            ReviewGroupDetailResponse(
                info = info,
                reviews = reviewSummaryQueryRepository.findReviews(reviewGroupId),
                targets = reviewTargetResultQueryRepository.findTargets(reviewGroupId),
                reviewers = reviewerInfoQueryRepository.findReviewers(reviewGroupId),
            )
        )
    }

    @Operation(summary = "리뷰 그룹 진행 현황 조회")
    @GetMapping("/{reviewGroupId}/progress")
    fun getReviewGroupProgress(
        @PathVariable reviewGroupId: Long,
    ): ResponseEntity<ReviewGroupProgressResponse> {
        val reviewGroupInfo = reviewGroupInfoQueryRepository.findInfo(reviewGroupId)
            ?: throw ReviewGroupNotFoundException()

        check (reviewGroupInfo.status == ReviewGroupStatus.IN_PROGRESS) { throw ReviewGroupNotFoundException("진행중인 리뷰 그룹을 찾을 수 없습니다.") }

        return ResponseEntity.ok(
            ReviewGroupProgressResponse(
                reviewGroupInfo = reviewGroupInfo,
                reviews = reviewProgressSummaryQueryRepository.findReviewProgress(reviewGroupId),
                reviewers = reviewerProgressInfoQueryRepository.findReviewerProgress(reviewGroupId),
            )
        )
    }

    @Operation(summary = "리뷰 상세 조회")
    @GetMapping("/{reviewGroupId}/reviews/{reviewId}")
    fun getReviewDetail(
        @PathVariable reviewGroupId: Long,
        @PathVariable reviewId: Long,
    ): ResponseEntity<ReviewDetailView> {
        reviewGroupInfoQueryRepository.findInfo(reviewGroupId)
            ?: throw ReviewGroupNotFoundException()
        return ResponseEntity.ok(
            reviewDetailQueryRepository.findDetail(reviewId)
                ?: throw ReviewGroupNotFoundException()
        )
    }


    @Operation(summary = "리뷰 그룹 결과 조회")
    @GetMapping("/{reviewGroupId}/results")
    fun getReviewGroupResult(
        @PathVariable reviewGroupId: Long,
    ): ResponseEntity<ReviewGroupResultResponse> {
        val reviewGroupInfo = reviewGroupInfoQueryRepository.findInfo(reviewGroupId)
            ?: throw ReviewGroupNotFoundException()

        check(reviewGroupInfo.status == ReviewGroupStatus.CLOSED)
            { throw ReviewGroupNotFoundException("마감된 리뷰 그룹을 찾을 수 없습니다.") }

        return ResponseEntity.ok(
            ReviewGroupResultResponse(
                reviewGroupInfo = reviewGroupInfo,
                targets = reviewTargetResultQueryRepository.findResults(reviewGroupId),
            )
        )
    }
}
