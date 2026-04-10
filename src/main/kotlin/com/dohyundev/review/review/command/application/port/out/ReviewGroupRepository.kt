package com.dohyundev.review.review.command.application.port.out

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewGroupRepository : JpaRepository<ReviewGroup, Long>
