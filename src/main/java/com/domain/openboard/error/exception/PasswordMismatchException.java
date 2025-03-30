package com.domain.openboard.error.exception;

import com.domain.openboard.error.CustomException;
import com.domain.openboard.error.ErrorCode;

public class PasswordMismatchException extends CustomException {
    public PasswordMismatchException() {
        super(ErrorCode.PASSWORD_MISMATCH);
    }
}
