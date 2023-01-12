package com.nyoongoon.fullstackjava.exception;

public class InvalidSiginInformation extends ProjectException{

    private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다.";

    public InvalidSiginInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
