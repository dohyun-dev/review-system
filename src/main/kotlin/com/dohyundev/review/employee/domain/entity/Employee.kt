package com.dohyundev.review.employee.domain.entity

import com.dohyundev.review.common.entity.BaseAggregateRootEntity
import com.dohyundev.review.employee.domain.dto.ChangePasswordCommand
import com.dohyundev.review.employee.domain.dto.RegisterEmployeeCommand
import com.dohyundev.review.employee.domain.dto.UpdateEmployeeCommand
import com.dohyundev.review.employee.domain.service.PasswordEncoder
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import kr.co.modaoutlet.mgr.employee.domain.EmployeeStatus
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.NaturalIdCache
import java.time.LocalDate

@Entity
@NaturalIdCache
class Employee(
    @Id
    @Tsid
    var id: Long? = null,

    @NaturalId
    @Column(nullable = false, unique = true)
    var no: String,

    @Column(nullable = false)
    var name: String,

    @Embedded
    var password: Password,

    @Column
    var hireDate: LocalDate,

    @Enumerated(EnumType.STRING)
    var role: EmployeeRole = EmployeeRole.USER,

    @Enumerated(EnumType.STRING)
    var status: EmployeeStatus = EmployeeStatus.ACTIVE,
): BaseAggregateRootEntity<Employee>() {

    companion object {
        const val DEFAULT_PASSWORD = "0000"

        fun register(command: RegisterEmployeeCommand, passwordEncoder: PasswordEncoder): Employee {
            return Employee(
                no = command.no,
                name = command.name,
                password = passwordEncoder.encode(DEFAULT_PASSWORD),
                hireDate = command.hireDate,
                role = command.role,
            )
        }
    }

    fun update(
        command: UpdateEmployeeCommand,
    ) {
        this.name = command.name
        this.role = command.role
        this.hireDate = command.hireDate
        this.status = command.status
    }

    fun changePassword(
        command: ChangePasswordCommand,
        passwordEncoder: PasswordEncoder
    ) {
        this.password = passwordEncoder.encode(command.password)
    }
}