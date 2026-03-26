package com.dohyundev.review.organization.member.application.port.service

import com.dohyundev.review.duty.application.port.out.DutyRepository
import com.dohyundev.review.employee.application.port.out.EmployeeRepository
import com.dohyundev.review.employee.domain.exception.EmployeeNotFoundException
import com.dohyundev.review.organization.department.application.port.out.DepartmentRepository
import com.dohyundev.review.organization.department.domain.exception.DepartmentNotFoundException
import com.dohyundev.review.organization.member.application.port.`in`.AppendDepartmentMemberUseCase
import com.dohyundev.review.organization.member.application.port.`in`.RemoveDepartmentMemberUseCase
import com.dohyundev.review.organization.member.application.port.`in`.UpdateDepartmentMemberUseCase
import com.dohyundev.review.organization.member.application.port.dto.AppendDepartmentMemberCommand
import com.dohyundev.review.organization.member.application.port.dto.UpdateDepartmentMemberCommand
import com.dohyundev.review.organization.member.application.port.out.DepartmentMemberRepository
import com.dohyundev.review.organization.member.domain.entity.DepartmentMember
import com.dohyundev.review.organization.member.domain.entity.DepartmentMemberType
import com.dohyundev.review.organization.member.domain.exception.AlreadyPrimaryDepartmentExistsException
import com.dohyundev.review.organization.member.domain.exception.DepartmentMemberNotFoundException
import com.dohyundev.review.organization.member.domain.exception.DuplicateDepartmentMemberException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DepartmentMemberCommandService(
    private val departmentMemberRepository: DepartmentMemberRepository,
    private val departmentRepository: DepartmentRepository,
    private val employeeRepository: EmployeeRepository,
    private val dutyRepository: DutyRepository,
) : AppendDepartmentMemberUseCase, UpdateDepartmentMemberUseCase, RemoveDepartmentMemberUseCase {

    override fun append(command: AppendDepartmentMemberCommand): DepartmentMember {
        val department = departmentRepository.findById(command.departmentId)
            .orElseThrow { DepartmentNotFoundException() }

        val employee = employeeRepository.findById(command.employeeId)
            .orElseThrow { EmployeeNotFoundException() }

        val duty = dutyRepository.findById(command.dutyId)
            .orElseThrow { NoSuchElementException("직책이 존재하지 않습니다.") }

        if (departmentMemberRepository.findByDepartmentIdAndEmployeeId(command.departmentId, command.employeeId).isPresent) {
            throw DuplicateDepartmentMemberException()
        }

        val member = DepartmentMember.create(
            department = department,
            employee = employee,
            duty = duty,
            type = command.type,
        )

        return departmentMemberRepository.save(member)
    }

    override fun update(id: Long, command: UpdateDepartmentMemberCommand): DepartmentMember {
        val member = departmentMemberRepository.findById(id)
            .orElseThrow { DepartmentMemberNotFoundException() }

        val duty = dutyRepository.findById(command.dutyId)
            .orElseThrow { NoSuchElementException("직책이 존재하지 않습니다.") }

        if (command.type == DepartmentMemberType.PRIMARY) {
            val existingPrimary = departmentMemberRepository.findByEmployeeIdAndType(member.employee.id!!, DepartmentMemberType.PRIMARY)
            if (existingPrimary.isPresent && existingPrimary.get().id != id) {
                throw AlreadyPrimaryDepartmentExistsException()
            }
        }

        member.update(duty = duty, type = command.type)

        return departmentMemberRepository.save(member)
    }

    override fun remove(id: Long) {
        val member = departmentMemberRepository.findById(id)
            .orElseThrow { DepartmentMemberNotFoundException() }

        departmentMemberRepository.delete(member)
    }
}
