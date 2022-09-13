package com.nyoongoon.fullstackjava.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/*
    {
        "code" : "400",
        "message" : "잘못된 요청입니다."
        "validation" : {
            "title" : "값을 입력해주세요."
         }
    }
 */
@Getter
//@JsonInclude(value =  JsonInclude.Include.NON_EMPTY) // 비어있어도 보내는 것을 추천함.
public class ErrorResponse { // 에러 포맷 커스텀 정의 => 내용 각자 다름
    private final String code;
    private final String message;
    private final Map<String, String> validation; // Map의 형태는 좋지 않으므로 추후 개선 필요 !

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation)  {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
