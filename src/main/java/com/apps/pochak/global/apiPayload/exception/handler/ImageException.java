package com.apps.pochak.global.apiPayload.exception.handler;

import com.apps.pochak.global.apiPayload.code.BaseErrorCode;
import com.apps.pochak.global.apiPayload.exception.GeneralException;

public class ImageException extends GeneralException {
    public ImageException(BaseErrorCode code) {
        super(code);
    }
}
