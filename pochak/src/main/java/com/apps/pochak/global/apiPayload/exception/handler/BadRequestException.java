package com.apps.pochak.global.apiPayload.exception.handler;

import com.apps.pochak.global.apiPayload.code.BaseErrorCode;
import com.apps.pochak.global.apiPayload.exception.GeneralException;

public class BadRequestException extends GeneralException {
    public BadRequestException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
