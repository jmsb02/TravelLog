package com.travellog.exception;

public class InvalidSigninInformation extends TravelLogException{

    private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다";

    public InvalidSigninInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}