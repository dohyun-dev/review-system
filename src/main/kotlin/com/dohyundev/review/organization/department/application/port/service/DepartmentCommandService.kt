package com.dohyundev.review.organization.department.application.port.service

import com.dohyundev.review.organization.department.application.port.`in`.CreateDepartmentUseCase
import com.dohyundev.review.organization.department.application.port.`in`.UpdateDepartmentUseCase
import com.dohyundev.review.organization.department.application.port.dto.CreateDepartmentCommand
import com.dohyundev.review.organization.department.application.port.dto.UpdateDepartmentCommand
import com.dohyundev.review.organization.department.application.port.out.DepartmentRepository
import com.dohyundev.review.organization.department.domain.entity.Department
import com.dohyundev.review.organization.department.domain.exception.DepartmentNotFoundException
import com.dohyundev.review.organization.department.domain.exception.DuplicateDepartmentNameException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DepartmentCommandService(
    private val departmentRepository: DepartmentRepository,
) : CreateDepartmentUseCase, UpdateDepartmentUseCase {

    override fun create(command: CreateDepartmentCommand): Department {
        val parent = command.parentId?.let {
            departmentRepository.findById(it)
                .orElseThrow { DepartmentNotFoundException("상위 부서가 존재하지 않습니다.") }
        }

        validateDuplicateName(command.name, command.parentId)

        val department = Department.create(
            name = command.name,
            parent = parent,
        )

        return departmentRepository.save(department)
    }

    override fun update(id: Long, command: UpdateDepartmentCommand): Department {
        val department = departmentRepository.findById(id)
            .orElseThrow { DepartmentNotFoundException() }

        val parent = command.parentId?.let {
            departmentRepository.findById(it).orElseThrow { DepartmentNotFoundException("상위 부서가 존재하지 않습니다.") }
        }

        if (department.name != command.name || department.parent?.id != command.parentId) {
            validateDuplicateName(command.name, command.parentId)
        }

        department.update(
            name = command.name,
            parent = parent,
        )

        return departmentRepository.save(department)
    }

    private fun validateDuplicateName(name: String, parentId: Long?) {
        if (departmentRepository.findByNameAndParentId(name, parentId).isPresent) {
            throw DuplicateDepartmentNameException()
        }
    }
}
