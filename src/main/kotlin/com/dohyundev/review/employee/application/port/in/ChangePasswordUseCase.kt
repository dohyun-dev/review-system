package kr.co.modaoutlet.mgr.employee.application.port.`in`

import kr.co.modaoutlet.mgr.employee.application.dto.ChangePasswordCommand

interface ChangePasswordUseCase {
    fun changePassword(id: Long, command: ChangePasswordCommand)
}
