package kr.co.modaoutlet.mgr.employee.application.port.`in`

import kr.co.modaoutlet.mgr.employee.application.dto.UpdateEmployeeCommand
import kr.co.modaoutlet.mgr.employee.domain.Employee

interface UpdateEmployeeUseCase {
    fun update(id: Long, command: UpdateEmployeeCommand): Employee
}
