package com.dohyundev.review.organization.department.domain.entity

import com.dohyundev.review.common.entity.BaseAggregateRootEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["name", "parent_id"])])
class Department(
    @Id
    @Tsid
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Department? = null,
): BaseAggregateRootEntity<Department>() {

    companion object {
        fun create(name: String, parent: Department? = null): Department {
            return Department(
                name = name,
                parent = parent,
            )
        }
    }

    fun update(name: String, parent: Department?) {
        this.name = name
        this.parent = parent
    }
}
