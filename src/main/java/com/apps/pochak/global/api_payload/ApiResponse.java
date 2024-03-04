package com.apps.pochak.global.api_payload;

import com.apps.pochak.global.api_payload.code.BaseCode;
import com.apps.pochak.global.api_payload.code.BaseErrorCode;
import com.apps.pochak.global.api_payload.code.ErrorReasonDTO;
import com.apps.pochak.global.api_payload.code.ReasonDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.apps.pochak.global.api_payload.code.status.SuccessStatus._OK;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;


    // success
    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, _OK.getCode(), _OK.getMessage(), result);
    }

    public static <T> ApiResponse<T> of(BaseCode code) {
        final ReasonDTO reasonDTO = code.getReasonHttpStatus();
        return new ApiResponse<>(true, reasonDTO.getCode(), reasonDTO.getMessage(), null);
    }

    public static <T> ApiResponse<T> of(BaseCode code, T result) {
        final ReasonDTO reasonDTO = code.getReasonHttpStatus();
        return new ApiResponse<>(true, reasonDTO.getCode(), reasonDTO.getMessage(), result);
    }

    // fail
    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(true, code, message, data);
    }

    public static <T> ApiResponse<T> of(BaseErrorCode code, T result) {
        final ErrorReasonDTO reasonHttpStatus = code.getReasonHttpStatus();
        return new ApiResponse<>(true, reasonHttpStatus.getCode(), reasonHttpStatus.getMessage(), result);
    }
}
