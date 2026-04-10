package com.dohyundev.review.employee.command.application

import com.dohyundev.review.employee.command.port.`in`.ChangePasswordUseCase
import com.dohyundev.review.employee.command.port.`in`.RegisterEmployeeUseCase
import com.dohyundev.review.employee.command.port.`in`.ResetPasswordUseCase
import com.dohyundev.review.employee.command.port.`in`.UpdateEmployeeUseCase
import com.dohyundev.review.employee.command.port.`in`.command.ChangePasswordCommand
import com.dohyundev.review.employee.command.port.`in`.command.RegisterEmployeeCommand
import com.dohyundev.review.employee.command.port.`in`.command.ResetPasswordCommand
import com.dohyundev.review.employee.command.port.`in`.command.UpdateEmployeeCommand
import com.dohyundev.review.employee.command.port.out.EmployeeRepository
import com.dohyundev.review.employee.command.domain.Employee
import com.dohyundev.review.employee.command.domain.EmployeeNo
import com.dohyundev.review.employee.command.domain.Password
import com.dohyundev.review.employee.command.domain.PasswordEncoder
import com.dohyundev.review.employee.command.domain.exception.DuplicateEmployeeNoException
import com.dohyundev.review.employee.command.domain.exception.EmployeeNotFoundException
import com.dohyundev.review.employee.command.domain.exception.InvalidPasswordException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class EmployeeCommandService(
    private val employeeRepository: EmployeeRepository,
    private val passwordEncoder: PasswordEncoder
) : RegisterEmployeeUseCase, UpdateEmployeeUseCase, ChangePasswordUseCase, ResetPasswordUseCase {

    override fun register(command: RegisterEmployeeCommand): Long {
        val no = EmployeeNo.create(command.employeeNo)

        if (employeeRepository.findByNo(no).isPresent) {
            throw DuplicateEmployeeNoException()
        }

        val password = passwordEncoder.encode(Password.DEFAULT_PASSWORD)

        val employee = Employee(
            no = no,
            name = command.name,
            hireDate = command.hireDate,
            password = password,
            role = command.role
        )

        return employeeRepository.save(employee).id!!
    }

    override fun update(command: UpdateEmployeeCommand) {
        val employee = employeeRepository.findById(command.employeeId)
            .orElseThrow { EmployeeNotFoundException() }

        employee.update(
            name = command.name,
            hireDate = command.hireDate,
            role = command.role,
            status = command.status
        )
    }

    override fun changePassword(command: ChangePasswordCommand) {
        val employee = employeeRepository.findById(command.employeeId)
            .orElseThrow { EmployeeNotFoundException() }

        if (!passwordEncoder.matches(command.currentPassword, employee.password)) {
            throw InvalidPasswordException()
        }

        employee.changePassword(passwordEncoder.encode(command.newPassword))
    }

    override fun resetPassword(command: ResetPasswordCommand) {
        val employee = employeeRepository.findById(command.employeeId)
            .orElseThrow { EmployeeNotFoundException() }

        employee.changePassword(passwordEncoder.encode(Password.DEFAULT_PASSWORD))
    }
}
