package com.dohyundev.review.review.group.domain

import com.dohyundev.review.common.entity.BaseEntity
import com.dohyundev.review.employee.domain.entity.Employee
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class ReviewTarget(
    @Id @Tsid
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_group_id", nullable = false)
    var reviewGroup: ReviewGroup? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    val employee: Employee,
) : BaseEntity() {

    companion object {
        fun create(employee: Employee): ReviewTarget {
            return ReviewTarget(employee = employee)
        }
    }
}
