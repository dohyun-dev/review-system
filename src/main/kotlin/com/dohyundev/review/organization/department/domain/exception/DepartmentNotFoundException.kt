package com.dohyundev.review.organization.department.domain.exception

import java.util.NoSuchElementException

class DepartmentNotFoundException(
    message: String = "부서가 존재하지 않습니다."
) : NoSuchElementException(message)
