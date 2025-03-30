package com.domain.openboard.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 에러 메시지 응답용 객체
public class ErrorResponse {

    private String message;
    private String code;
}
