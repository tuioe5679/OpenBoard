package com.domain.openboard.exception;

import lombok.Getter;

@Getter
// RuntimeException을 상속한 사용자 정의 예외 클래스
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 부모 클래스에 전달
        this.errorCode = errorCode;
    }
}
