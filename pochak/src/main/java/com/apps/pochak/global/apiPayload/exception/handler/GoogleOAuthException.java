package com.apps.pochak.global.apiPayload.exception.handler;

import com.apps.pochak.global.apiPayload.code.BaseErrorCode;
import com.apps.pochak.global.apiPayload.exception.GeneralException;

public class GoogleOAuthException extends GeneralException {
    public GoogleOAuthException(BaseErrorCode code) {
        super(code);
    }
}
