package com.linkpay.userBll.exception;

import com.linkpay.commonModule.constant.ResponseCode;
import com.linkpay.commonModule.exception.NoPermissionException;
import com.linkpay.commonModule.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
@RestControllerAdvice
public class GlobalException {

    private static final Logger log = LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler
    public Result handle(Exception e) {
        if (e instanceof RollbackException) {
            RollbackException rollbackException = (RollbackException) e;
            return new Result()
                    .responseCode(rollbackException.getResponseCode())
                    .message(rollbackException.getMessage());
        } else if (e instanceof NoPermissionException) {
            log.info("no permission：{}", e.toString(), e);
            NoPermissionException noPermissionException = (NoPermissionException) e;
            return new Result()
                .responseCode(ResponseCode.NO_PERMISSION);
        }
        log.error("系统异常：{}", e.toString(), e);
        return new Result().responseCode(ResponseCode.SYSTEM_ERROR);
    }


}
