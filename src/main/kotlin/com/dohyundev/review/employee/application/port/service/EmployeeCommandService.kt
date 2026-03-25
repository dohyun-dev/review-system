package com.dohyundev.review.employee.application.port.service

import com.dohyundev.review.employee.application.port.`in`.ChangePasswordUseCase
import com.dohyundev.review.employee.application.port.`in`.RegisterEmployeeUseCase
import com.dohyundev.review.employee.application.port.`in`.UpdateEmployeeUseCase
import com.dohyundev.review.employee.domain.dto.ChangePasswordCommand
import com.dohyundev.review.employee.domain.dto.RegisterEmployeeCommand
import com.dohyundev.review.employee.domain.dto.UpdateEmployeeCommand
import com.dohyundev.review.employee.application.port.out.EmployeeRepository
import com.dohyundev.review.employee.domain.entity.Employee
import com.dohyundev.review.employee.domain.exception.DuplicateEmployeeNoException
import com.dohyundev.review.employee.domain.exception.EmployeeNotFoundException
import com.dohyundev.review.employee.domain.service.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class EmployeeCommandService(
    private val employeeRepository: EmployeeRepository,
    private val passwordEncoder: PasswordEncoder,
): RegisterEmployeeUseCase, UpdateEmployeeUseCase, ChangePasswordUseCase {
    override fun register(command: RegisterEmployeeCommand): Employee {
        if (employeeRepository.findByNo(command.no).isPresent) {
            throw DuplicateEmployeeNoException()
        }

        val employee = Employee.register(command, passwordEncoder)

        return employeeRepository.save(employee)
    }

    override fun update(
        id: Long,
        command: UpdateEmployeeCommand
    ): Employee {
        val employee = employeeRepository.findById(id)
            .orElseThrow { EmployeeNotFoundException() }

        employee.update(command)

        return employeeRepository.save(employee)
    }

    override fun changePassword(id: Long, command: ChangePasswordCommand): Employee {
        val employee = employeeRepository.findById(id)
            .orElseThrow { EmployeeNotFoundException() }

        employee.changePassword(command, passwordEncoder)

        return employeeRepository.save(employee)
    }
}