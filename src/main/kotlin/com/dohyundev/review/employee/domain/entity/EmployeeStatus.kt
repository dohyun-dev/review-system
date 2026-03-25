package kr.co.modaoutlet.mgr.employee.domain

enum class EmployeeStatus(
    val displayName: String,
) {
    ACTIVE("재직"),
    ON_LEAVE("휴직"),
    INACTIVE("퇴사"),
}