package com.linkpay.userBll.service;


import com.linkpay.userBll.api.ApiUtil;
import com.linkpay.userBll.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    AuthCaptchaGetResponse authCaptchaGet(AuthCaptchaGetRequest authCaptchaGetRequest);

    AuthLoginResponse authLogin(AuthLoginRequest authLoginRequest);

    AuthLogoutResponse authLogout(AuthLogoutRequest authLogoutRequest);

    AuthSendVerificationCodeResponse authSendVerificationCode(AuthSendVerificationCodeRequest authSendVerificationCodeRequest);

}
