package com.linkpay.userBll.controller;

import cn.hutool.jwt.JWTHeader;
import com.linkpay.commonModule.constant.ResponseCode;
import com.linkpay.commonModule.exception.NoPermissionException;
import com.linkpay.commonModule.jwt.JwtRole;
import com.linkpay.commonModule.jwt.JwtStatusCode;
import com.linkpay.commonModule.jwt.JwtTokenHeaderKey;
import com.linkpay.commonModule.jwt.JwtUtil;
import com.linkpay.commonModule.result.ResultUtil;
import com.linkpay.userBase.api.UserApi;
import com.linkpay.userBase.model.User;
import com.linkpay.userBll.api.ApiUtil;
import com.linkpay.userBll.api.AuthApi;
import com.linkpay.userBll.api.AuthApiDelegate;
import com.linkpay.userBll.api.UserApiDelegate;
import com.linkpay.userBll.model.*;
import com.linkpay.userBll.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;


@Component
@Slf4j
public class AuthApiControllerImpl implements AuthApiDelegate {

    @Autowired
    AuthService authService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private NativeWebRequest nativeWebRequest;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<AuthLoginResponse> authLogin(AuthLoginRequest authLoginRequest) {
        return new ResponseEntity<>(authService.authLogin(authLoginRequest), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<AuthLogoutResponse> authLogout(AuthLogoutRequest authLogoutRequest)  {

        var request = getRequest();
        var authorizationHeader = request.get().getHeader(JwtTokenHeaderKey.AUTHORIZATION);;
        var accessToken = (String)null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            accessToken = authorizationHeader.substring(7);
        }
        else {
            log.info("no permission. authorization: {}", authorizationHeader);
            throw new NoPermissionException();
        }

        var refreshToken = request.get().getHeader(JwtTokenHeaderKey.REFRESH_TOKEN);

        var authLogoutResponse = new AuthLogoutResponse();

        if (!ObjectUtils.isEmpty(accessToken)) {
            var validateAccessTokenResult = jwtUtil.validateAccessToken(accessToken);
            if (!validateAccessTokenResult.getSuccess()) {
                log.info("jwt access token validate result: [{}]", validateAccessTokenResult);
                throw new NoPermissionException();
            }

            var authLogoutRequestAccessToken = new AuthLogoutRequestAccessToken();
            validateAccessTokenResult.getData().forEach((k, v) -> authLogoutRequestAccessToken.putAdditionalProperty(k, v));

            authLogoutRequest.accessToken(authLogoutRequestAccessToken);
            log.info("get access token claim: {}", validateAccessTokenResult.getData());
        }

        if (!ObjectUtils.isEmpty(refreshToken)) {
            var validateAccessTokenResult = jwtUtil.validateAccessToken(refreshToken);
            if (!validateAccessTokenResult.getSuccess()) {
                log.info("jwt refresh token validate result: {}", validateAccessTokenResult);
                var result = ResultUtil.error(
                        authLogoutResponse,
                        ResponseCode.REFRESH_TOKEN_VALIDATION_FAILED);

                return new ResponseEntity<AuthLogoutResponse>(result, OK);
            }

            var authLogoutRequestRefreshToken = new AuthLogoutRequestAccessToken();
            validateAccessTokenResult.getData().forEach((k, v) -> authLogoutRequestRefreshToken.putAdditionalProperty(k, v));

            authLogoutRequest.refreshToken(authLogoutRequestRefreshToken);
            log.info("get refresh token claim: {}", validateAccessTokenResult.getData());
        }
        return new ResponseEntity<>(authService.authLogout(authLogoutRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AuthSendVerificationCodeResponse> authSendVerificationCode(AuthSendVerificationCodeRequest authSendVerificationCodeRequest) {
        return new ResponseEntity<>(authService.authSendVerificationCode(authSendVerificationCodeRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AuthCaptchaGetResponse> authCaptchaGet(AuthCaptchaGetRequest authCaptchaGetRequest) {
        return new ResponseEntity<>(authService.authCaptchaGet(authCaptchaGetRequest), HttpStatus.OK);
    }
}