package org.taskservice.exceptions.advice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.taskservice.dto.ErrorInfoDto;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfoDto handleMethodArgumentNotValidException(HttpServletRequest req,
                                                              MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ErrorInfoDto(req.getRequestURL().toString(), errors.values().toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorInfoDto handleMethodArgumentTypeMismatchException(HttpServletRequest req,
                                                                  MethodArgumentTypeMismatchException e) {
        String error = e.getName() + " should be of type " + e.getRequiredType().getName();
        return new ErrorInfoDto(req.getRequestURL().toString(), error);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorInfoDto handleHttpMessageNotReadableException(HttpServletRequest req,
                                                              HttpMessageNotReadableException e) {
        return new ErrorInfoDto(req.getRequestURL().toString(), e.getMessage());
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorInfoDto handleEntityNotFoundException(HttpServletRequest req,
                                                      EntityNotFoundException e) {
        return new ErrorInfoDto(req.getRequestURL().toString(), e.getMessage());
    }
}