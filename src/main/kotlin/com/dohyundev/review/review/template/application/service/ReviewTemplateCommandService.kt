package com.dohyundev.review.review.template.application.service

import com.dohyundev.review.review.template.application.factory.ReviewTemplateFactory
import com.dohyundev.review.review.template.application.factory.ReviewTemplateSectionFactory
import com.dohyundev.review.review.template.application.dto.CreateReviewTemplateCommand
import com.dohyundev.review.review.template.application.dto.UpdateReviewTemplateCommand
import com.dohyundev.review.review.template.application.dto.UpdateReviewTemplateFormCommand
import com.dohyundev.review.review.template.application.port.`in`.CreateReviewTemplateUseCase
import com.dohyundev.review.review.template.application.port.`in`.DeleteReviewTemplateUseCase
import com.dohyundev.review.review.template.application.port.`in`.UpdateFormReviewTemplateUseCase
import com.dohyundev.review.review.template.application.port.`in`.UpdateReviewTemplateUseCase
import com.dohyundev.review.review.template.application.port.out.ReviewTemplateRepository
import com.dohyundev.review.review.template.domain.ReviewTemplate
import com.dohyundev.review.review.template.domain.exception.ReviewTemplateNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewTemplateCommandService(
    private val reviewTemplateRepository: ReviewTemplateRepository,
    private val reviewTemplateFactory: ReviewTemplateFactory,
    private val reviewTemplateSectionFactory: ReviewTemplateSectionFactory,
) : CreateReviewTemplateUseCase, UpdateFormReviewTemplateUseCase, UpdateReviewTemplateUseCase, DeleteReviewTemplateUseCase {

    override fun create(command: CreateReviewTemplateCommand): ReviewTemplate {
        val sortOrder = reviewTemplateRepository.findMaxSortOrder() + 1
        return reviewTemplateRepository.save(reviewTemplateFactory.create(command, sortOrder))
    }

    override fun update(id: Long, command: UpdateReviewTemplateCommand): ReviewTemplate {
        val template = reviewTemplateRepository.findById(id).orElseThrow { ReviewTemplateNotFoundException() }
        template.update(
            name = command.name,
            description = command.description,
            sortOrder = command.sortOrder,
        )
        return reviewTemplateRepository.save(template)
    }

    override fun updateForm(id: Long, command: UpdateReviewTemplateFormCommand): ReviewTemplate {
        val template = reviewTemplateRepository.findById(id).orElseThrow { ReviewTemplateNotFoundException() }
        template.updateForm(
            sections = command.sections.map { reviewTemplateSectionFactory.create(it) },
        )
        return reviewTemplateRepository.save(template)
    }

    override fun delete(id: Long) {
        val template = reviewTemplateRepository.findById(id).orElseThrow { ReviewTemplateNotFoundException() }
        reviewTemplateRepository.delete(template)
    }
}
