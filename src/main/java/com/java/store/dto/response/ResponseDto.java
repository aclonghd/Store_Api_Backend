package com.java.store.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private final int code;
    private final String message;
    private Object result;

    public ResponseDto(int code, String message,Object object){
        timestamp = LocalDateTime.now();
        this.code = code;
        this.message = message;
        this.result = object;
    }

    public ResponseDto(int code, String message) {
        timestamp = LocalDateTime.now();
        this.code = code;
        this.message = message;
        this.result = null;
    }
}
