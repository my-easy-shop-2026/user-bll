package com.linkpay.userBll.exception;


import com.linkpay.commonModule.constant.ResponseCode;
import lombok.Getter;

public class RollbackException extends RuntimeException {

    @Getter
    ResponseCode responseCode;

    public RollbackException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public RollbackException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }
}
