package com.dohyundev.review.review.template.application.port.out

import com.dohyundev.review.review.template.domain.ReviewTemplate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReviewTemplateRepository : JpaRepository<ReviewTemplate, Long> {

    @Query("SELECT COALESCE(MAX(t.sortOrder), 0) FROM ReviewTemplate t")
    fun findMaxSortOrder(): Int
}
