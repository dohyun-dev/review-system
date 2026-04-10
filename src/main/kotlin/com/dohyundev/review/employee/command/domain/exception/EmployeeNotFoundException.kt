package com.dohyundev.review.employee.command.domain.exception

import java.util.NoSuchElementException

class EmployeeNotFoundException(
    message: String = "사원이 존재하지 않습니다."
) : NoSuchElementException(message)