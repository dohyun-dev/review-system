package com.dohyundev.review.employee.application.port.`in`

import com.dohyundev.review.employee.application.port.out.EmployeeRepository
import com.dohyundev.review.employee.application.port.service.EmployeeCommandService
import com.dohyundev.review.employee.domain.dto.ChangePasswordCommand
import com.dohyundev.review.employee.domain.exception.EmployeeNotFoundException
import com.dohyundev.review.employee.fixture.EmployeeFixture
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class ChangePasswordUseCaseTest {

    @Autowired
    lateinit var employeeCommandService: EmployeeCommandService

    @Autowired
    lateinit var employeeRepository: EmployeeRepository

    @Test
    fun `비밀번호를 인코딩하여 변경한다`() {
        val employee = employeeRepository.save(EmployeeFixture.create(no = "EMP001"))
        val originalPassword = employee.password.value
        val command = ChangePasswordCommand(password = "newPass123")

        val updated = employeeCommandService.changePassword(employee.id!!, command)

        assertNotEquals(originalPassword, updated.password.value)
        assertNotEquals("newPass123", updated.password.value)
    }

    @Test
    fun `존재하지 않는 사원 ID면 EmployeeNotFoundException을 던진다`() {
        assertThrows<EmployeeNotFoundException> {
            employeeCommandService.changePassword(999999L, ChangePasswordCommand("pass"))
        }
    }

}
