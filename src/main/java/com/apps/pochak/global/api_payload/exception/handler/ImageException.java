package com.apps.pochak.global.api_payload.exception.handler;

import com.apps.pochak.global.api_payload.code.BaseErrorCode;
import com.apps.pochak.global.api_payload.exception.GeneralException;

public class ImageException extends GeneralException {
    public ImageException(BaseErrorCode code) {
        super(code);
    }
}
