package kr.co.modaoutlet.mgr.employee.domain.exception

import java.util.NoSuchElementException

class EmployeeNotFoundException(
    message: String = "직원이 존재하지 않습니다."
) : NoSuchElementException(message)