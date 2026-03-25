package com.dohyundev.review.duty.application.port.out

import com.dohyundev.review.duty.domain.Duty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface DutyRepository : JpaRepository<Duty, Long> {
    fun findByName(name: String): Optional<Duty>

    @Query("SELECT COALESCE(MAX(d.sortOrder), 0) FROM Duty d")
    fun findMaxSortOrder(): Int
}
