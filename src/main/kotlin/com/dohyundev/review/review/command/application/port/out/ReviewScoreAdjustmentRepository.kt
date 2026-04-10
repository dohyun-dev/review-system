package com.dohyundev.review.review.command.application.port.out

import com.dohyundev.review.review.command.domain.score.ReviewScoreAdjustment
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewScoreAdjustmentRepository : JpaRepository<ReviewScoreAdjustment, Long>
