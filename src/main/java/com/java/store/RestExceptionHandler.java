package com.java.store;

import com.java.store.dto.response.ResponseDto;
import com.java.store.exception.ServiceException;
import org.springframework.http.HttpHeaders;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseDto responseDto = new ResponseDto(BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(responseDto, BAD_REQUEST);
    }
    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<Object> handleServiceException(ServiceException ex){
        ResponseDto responseDto = new ResponseDto(BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(ex.getCode()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseDto responseDto = new ResponseDto(BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(responseDto, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseDto responseDto = new ResponseDto(BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(responseDto, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnwantedException(Exception e) {
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
