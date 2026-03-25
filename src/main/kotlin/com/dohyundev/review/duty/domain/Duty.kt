package com.dohyundev.review.duty.domain

import com.dohyundev.review.common.SortOrderable
import com.dohyundev.review.common.entity.BaseAggregateRootEntity
import com.dohyundev.review.duty.domain.dto.UpdateDutyCommand
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*

@Entity
class Duty(
    @Id
    @Tsid
    var id: Long? = null,

    @Column(name = "name", nullable = false, unique = true)
    var name: String,

    @Column(name = "sort_order", nullable = false)
    override var sortOrder: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: DutyStatus = DutyStatus.ACTIVE,
) : BaseAggregateRootEntity<Duty>(), SortOrderable<Duty> {

    companion object {
        fun create(
            name: String,
            sortOrder: Int,
        ): Duty {
            return Duty(
                name = name,
                sortOrder = sortOrder,
            )
        }
    }

    fun update(
        command: UpdateDutyCommand
    ) {
        this.name = command.name
        this.sortOrder = command.sortOrder
        this.status = command.status
    }
}
