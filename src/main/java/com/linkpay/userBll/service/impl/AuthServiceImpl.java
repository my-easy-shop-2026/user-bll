package com.linkpay.userBll.service.impl;

import com.linkpay.userBase.api.AuthApi;
import com.linkpay.userBll.api.ApiUtil;
import com.linkpay.userBll.model.*;
import com.linkpay.userBll.service.AuthService;
import com.linkpay.userBll.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthApi authApi;

    @Override
    public AuthCaptchaGetResponse authCaptchaGet(AuthCaptchaGetRequest authCaptchaGetRequest) {

        var authCaptchaGetRequestCopy = new com.linkpay.userBase.model.AuthCaptchaGetRequest();

        BeanUtils.copyProperties(authCaptchaGetRequest, authCaptchaGetRequestCopy);
        var authCaptchaGetResponse = authApi.authCaptchaGet(authCaptchaGetRequestCopy);
        if (!HttpStatus.OK.equals(authCaptchaGetResponse.getStatusCode())) {
            log.warn("fail to request userBase.authCaptchaGet, response: [{}]", authCaptchaGetResponse);
        }

        var resultBody = authCaptchaGetResponse.getBody();
        var result = new AuthCaptchaGetResponse();
        BeanUtils.copyProperties(resultBody, result);

        return result;
    }

    @Override
    public AuthLoginResponse authLogin(AuthLoginRequest authLoginRequest) {

        var authLoginRequestCopy = new com.linkpay.userBase.model.AuthLoginRequest();

        BeanUtils.copyProperties(authLoginRequest, authLoginRequestCopy);
        var authLoginResponse = authApi.authLogin(authLoginRequestCopy);
        if (!HttpStatus.OK.equals(authLoginResponse.getStatusCode())) {
            log.warn("fail to request userBase.authLogin, response: [{}]", authLoginResponse);
        }

        var resultBody = authLoginResponse.getBody();
        var result = new AuthLoginResponse();
        BeanUtils.copyProperties(resultBody, result);

        if (resultBody.getData() != null) {
            var resultData = new JwtToken();
            BeanUtils.copyProperties(resultBody.getData(), resultData);
            result.setData(resultData);
        }

        return result;
    }

    @Override
    public AuthLogoutResponse authLogout(AuthLogoutRequest authLogoutRequest) {

        var logoutRequest = new com.linkpay.userBase.model.AuthLogoutRequest();
        BeanUtils.copyProperties(authLogoutRequest, logoutRequest);

        if (authLogoutRequest.getAccessToken() != null) {
            var authLogoutRequestAccessToken = new com.linkpay.userBase.model.AuthLogoutRequestAccessToken();
            authLogoutRequest.getAccessToken().getAdditionalProperties().forEach((k, v) -> authLogoutRequestAccessToken.putAdditionalProperty(k, v));
            logoutRequest.setAccessToken(authLogoutRequestAccessToken);
        }

        if (authLogoutRequest.getRefreshToken() != null) {
            var authLogoutRequestRefreshToken = new com.linkpay.userBase.model.AuthLogoutRequestAccessToken();
            authLogoutRequest.getAccessToken().getAdditionalProperties().forEach((k, v) -> authLogoutRequestRefreshToken.putAdditionalProperty(k, v));
            logoutRequest.setRefreshToken(authLogoutRequestRefreshToken);
        }

        var logoutResponse = authApi.authLogout(logoutRequest);
        if (!HttpStatus.OK.equals(logoutResponse.getStatusCode())) {
            log.warn("fail to request userBase.authLogout, response: [{}]", logoutResponse);
        }

        var resultBody = logoutResponse.getBody();
        var result = new AuthLogoutResponse();
        BeanUtils.copyProperties(resultBody, result);

        return result;
    }

    @Override
    public AuthSendVerificationCodeResponse authSendVerificationCode(AuthSendVerificationCodeRequest authSendVerificationCodeRequest) {

        var authSendVerificationCodeRequestCopy = new com.linkpay.userBase.model.AuthSendVerificationCodeRequest();

        BeanUtils.copyProperties(authSendVerificationCodeRequest, authSendVerificationCodeRequestCopy);
        var authSendVerificationCodeResponse = authApi.authSendVerificationCode(authSendVerificationCodeRequestCopy);
        if (!HttpStatus.OK.equals(authSendVerificationCodeResponse.getStatusCode())) {
            log.warn("fail to request userBase.authSendVerificationCode, response: [{}]", authSendVerificationCodeResponse);
        }

        var resultBody = authSendVerificationCodeResponse.getBody();
        var result = new AuthSendVerificationCodeResponse();
        BeanUtils.copyProperties(resultBody, result);

        return result;
    }
}
