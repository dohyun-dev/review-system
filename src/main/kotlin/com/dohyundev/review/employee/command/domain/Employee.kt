package com.dohyundev.review.employee.command.domain

import jakarta.persistence.*
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.NaturalIdCache
import java.time.LocalDate

@Entity
@NaturalIdCache
class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    no: EmployeeNo,

    name: String,

    hireDate: LocalDate,

    password: Password,

    role: EmployeeRole
) {
    @NaturalId
    @Embedded
    @AttributeOverride(
        name = "number",
        column = Column(name = "no", nullable = false)
    )
    var no: EmployeeNo = no
        protected set

    @Column(nullable = false)
    var name: String = ""
        protected set(name) {
            require(name.isNotBlank() && name.length <= 10) { "이름은 비어 있을 수 없으며 10자 이하여야 합니다." }
            field = name
        }

    @Column(nullable = false)
    var hireDate: LocalDate = hireDate
        protected set

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "password_hash", nullable = false))
    var password: Password = password
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: EmployeeRole = role
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: EmployeeStatus = EmployeeStatus.ACTIVE
        protected set

    init {
        this.name = name
    }

    fun update(
        name: String,
        hireDate: LocalDate,
        role: EmployeeRole,
        status: EmployeeStatus,
    ) {
        if (this.status == EmployeeStatus.INACTIVE) {
            throw IllegalStateException("퇴사한 직원 정보는 수정할 수 없습니다.")
        }

        this.name = name
        this.hireDate = hireDate
        this.role = role
        this.status = status
    }

    fun changePassword(password: Password) {
        this.password = password
    }

    fun changeStatus(status: EmployeeStatus) {
        this.status = status
    }
}
