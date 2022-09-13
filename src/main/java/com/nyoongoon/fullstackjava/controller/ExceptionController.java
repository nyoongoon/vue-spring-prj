package com.nyoongoon.fullstackjava.controller;

import com.nyoongoon.fullstackjava.exception.InvalidRequest;
import com.nyoongoon.fullstackjava.exception.ProjectException;
import com.nyoongoon.fullstackjava.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@Slf4j
@ControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody // 붙이면 에러를 json 형태로 변환 가능
    public ErrorResponse invaildRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .validation(new HashMap<String, String>())
                .build();
        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }

    @ResponseBody
    @ExceptionHandler(ProjectException.class) // PostNotFound예외 발생시 캐치해줘서 클라에게 responseBody 전송
    public ResponseEntity<ErrorResponse> projectException(ProjectException e){
        int statusCode = e.getStatusCode();
        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        // 응답 json validation. -> title: 제목에 바보를 포함할 수 없음.

        // 디테일 추가

        // ResponseEntity<> => 바디와 응답코드를 수정해서 내려줌
        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);
        return response;
    }

}
