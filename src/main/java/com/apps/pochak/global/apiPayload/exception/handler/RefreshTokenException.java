package com.apps.pochak.global.apiPayload.exception.handler;

import com.apps.pochak.global.apiPayload.code.BaseErrorCode;
import com.apps.pochak.global.apiPayload.exception.GeneralException;

public class RefreshTokenException extends GeneralException {
    public RefreshTokenException(BaseErrorCode code) {
        super(code);
    }
}
