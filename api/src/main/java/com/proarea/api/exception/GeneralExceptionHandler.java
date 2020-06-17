package com.proarea.api.exception;


import com.proarea.api.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception ex, HttpServletRequest request) {
        log.error("URI={}, {}", request.getRequestURI(), ex);
        return ErrorResponse.of(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleException(EntityNotFoundException ex, HttpServletRequest request) {
        log.error("URI={}, {}", request.getRequestURI(), ex);
        return ErrorResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND.value(), request.getRequestURI());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("URI={}, {}", request.getRequestURI(), ex);
        return ErrorResponse.of(Messages.DATA_INTEGRITY_VIOLATION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(DuplicateKeyException ex, HttpServletRequest request) {
        log.error("URI={}, {}", request.getRequestURI(), ex);
        return ErrorResponse.of(Messages.DUPLICATE_KEY_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI());
    }

    @ExceptionHandler(UnparseableFilterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(UnparseableFilterException ex, HttpServletRequest request) {
        log.error("URI={}, {}", request.getRequestURI(), ex.getMessage());
        return ErrorResponse.of(Messages.UNPARSEABLE_FILTER, HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(BadRequestException ex, HttpServletRequest request) {
        log.error("URI={}, {}", request.getRequestURI(), ex);
        return ErrorResponse.of(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleException(AccessDeniedException ex, HttpServletRequest request) {
        log.error("URI={}, {}", request.getRequestURI(), ex);
        return ErrorResponse.of(ex.getMessage(), HttpStatus.FORBIDDEN.value(), request.getRequestURI());
    }

}
