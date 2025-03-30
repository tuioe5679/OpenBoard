package com.domain.openboard.error.exception;

import com.domain.openboard.error.CustomException;
import com.domain.openboard.error.ErrorCode;

public class PostNotFoundException extends CustomException {
    public PostNotFoundException() {
        super(ErrorCode.POST_NOT_FOUND);
    }
}
