package com.dohyundev.review.common.web

import com.dohyundev.review.common.exception.AuthException
import com.dohyundev.review.common.exception.ForbiddenException
import jakarta.validation.ConstraintViolationException
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalRestControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ProblemDetail {
        val detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        detail.title = "입력값이 올바르지 않습니다."
        detail.setProperty("errors", e.bindingResult.fieldErrors.map { error ->
            mapOf("field" to error.field, "message" to error.defaultMessage)
        })
        return detail
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException): ProblemDetail {
        val detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        detail.title = "입력값이 올바르지 않습니다."
        detail.setProperty("errors", e.constraintViolations.map { v ->
            mapOf("field" to v.propertyPath.toString(), "message" to v.message)
        })
        return detail
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(e: HttpMessageNotReadableException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")

    @ExceptionHandler(MissingPathVariableException::class)
    fun handleMissingPathVariable(e: MissingPathVariableException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "경로 변수 '${e.variableName}'이(가) 누락되었습니다.")

    @ExceptionHandler(TypeMismatchException::class)
    fun handleTypeMismatch(e: TypeMismatchException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "'${e.value}'은(는) 올바른 형식이 아닙니다.")

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message ?: "잘못된 요청입니다.")

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElement(e: NoSuchElementException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message ?: "리소스를 찾을 수 없습니다.")

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(e: IllegalStateException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message ?: "요청을 처리할 수 없습니다.")

    @ExceptionHandler(AuthException::class)
    fun handleAuth(e: AuthException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.message ?: "인증에 실패했습니다.")

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbidden(e: ForbiddenException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.")

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAuthorizationDenied(e: AuthorizationDeniedException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.")

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.")
}
