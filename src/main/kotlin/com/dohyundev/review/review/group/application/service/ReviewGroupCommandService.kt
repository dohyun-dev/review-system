package com.dohyundev.review.review.group.application.service

import com.dohyundev.review.review.group.application.dto.CreateReviewGroupCommand
import com.dohyundev.review.review.group.application.port.`in`.CreateReviewGroupUseCase
import com.dohyundev.review.review.group.application.port.`in`.DeleteReviewGroupUseCase
import com.dohyundev.review.review.group.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.group.domain.ReviewGroup
import com.dohyundev.review.review.group.domain.exception.ReviewGroupNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewGroupCommandService(
    private val reviewGroupRepository: ReviewGroupRepository,
) : CreateReviewGroupUseCase, DeleteReviewGroupUseCase {

    override fun create(command: CreateReviewGroupCommand): ReviewGroup {
        return reviewGroupRepository.save(
            ReviewGroup.create(name = command.name, description = command.description)
        )
    }

    override fun delete(id: Long) {
        val reviewGroup = reviewGroupRepository.findById(id).orElseThrow { ReviewGroupNotFoundException() }
        reviewGroupRepository.delete(reviewGroup)
    }
}
