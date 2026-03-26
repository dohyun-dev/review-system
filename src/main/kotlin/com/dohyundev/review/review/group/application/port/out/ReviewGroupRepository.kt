package com.dohyundev.review.review.group.application.port.out

import com.dohyundev.review.review.group.domain.ReviewGroup
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewGroupRepository : JpaRepository<ReviewGroup, Long>
