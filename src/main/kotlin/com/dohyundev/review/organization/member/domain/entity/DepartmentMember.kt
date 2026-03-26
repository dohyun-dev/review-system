package com.dohyundev.review.organization.member.domain.entity

import com.dohyundev.review.common.entity.BaseAggregateRootEntity
import com.dohyundev.review.duty.domain.Duty
import com.dohyundev.review.employee.domain.entity.Employee
import com.dohyundev.review.organization.department.domain.entity.Department
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import org.hibernate.annotations.NaturalId

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["department_id", "employee_id"])])
class DepartmentMember(
    @Id
    @Tsid
    var id: Long? = null,

    @NaturalId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    val department: Department,

    @NaturalId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    val employee: Employee,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duty_id", nullable = false)
    var duty: Duty,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: DepartmentMemberType,
): BaseAggregateRootEntity<DepartmentMember>() {

    companion object {
        fun create(
            department: Department,
            employee: Employee,
            duty: Duty,
            type: DepartmentMemberType,
        ): DepartmentMember {
            return DepartmentMember(
                department = department,
                employee = employee,
                duty = duty,
                type = type,
            )
        }
    }

    fun update(duty: Duty, type: DepartmentMemberType) {
        this.duty = duty
        this.type = type
    }
}
