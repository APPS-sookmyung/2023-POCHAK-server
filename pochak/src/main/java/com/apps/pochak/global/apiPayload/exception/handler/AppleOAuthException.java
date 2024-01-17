package com.apps.pochak.global.apiPayload.exception.handler;

import com.apps.pochak.global.apiPayload.code.BaseErrorCode;
import com.apps.pochak.global.apiPayload.exception.GeneralException;

public class AppleOAuthException extends GeneralException {
    public AppleOAuthException(BaseErrorCode code) {
        super(code);
    }
}
