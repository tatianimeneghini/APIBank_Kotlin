package com.example.bankapi.controller.handler

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ErrorHandler {

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun illegalArgumentException(
        request: HttpServletRequest,
        exception: Exception,
    ): ResponseEntity<ErrorResponse> {
        val statusCode = HttpStatus.BAD_REQUEST
        val errorResponse = ErrorResponse(statusCode = statusCode.value(), message = exception.message!!)
        return ResponseEntity.badRequest().body(errorResponse)
    }

}

class ErrorResponse(val statusCode: Int, val message: String)