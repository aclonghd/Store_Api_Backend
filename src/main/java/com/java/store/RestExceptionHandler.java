package com.java.store;

import com.java.store.dto.ResponseDto;
import org.springframework.http.HttpHeaders;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setCode(BAD_REQUEST.value());
        responseDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseDto, BAD_REQUEST);
    }
}
