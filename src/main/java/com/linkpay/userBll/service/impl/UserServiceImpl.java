package com.linkpay.userBll.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.linkpay.commonModule.constant.ResponseCode;
import com.linkpay.commonModule.result.ResultUtil;
import com.linkpay.commonModule.util.copyUtil.CopyUtil;
import com.linkpay.userBase.api.UserApi;
import com.linkpay.userBll.model.JwtToken;
import com.linkpay.userBll.model.UserForgetPasswordRequest;
import com.linkpay.userBll.model.UserForgetPasswordResponse;
import com.linkpay.userBll.model.UserInfoGetRequest;
import com.linkpay.userBll.model.UserInfoGetResponse;
import com.linkpay.userBll.model.UserRegisterRequest;
import com.linkpay.userBll.model.UserRegisterResponse;
import com.linkpay.userBll.model.UserResetPasswordRequest;
import com.linkpay.userBll.model.UserResetPasswordResponse;
import com.linkpay.userBll.service.UserService;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

  @Autowired
  private UserApi userApi;

  @Autowired
  private CopyUtil copyUtil;

  @Override
  public UserForgetPasswordResponse userForgetPassword(
      UserForgetPasswordRequest userForgetPasswordRequest) {

    var userForgetPasswordRequestCopy = new com.linkpay.userBase.model.UserForgetPasswordRequest();

    BeanUtils.copyProperties(userForgetPasswordRequest, userForgetPasswordRequestCopy);

    var userForgetPasswordResponse = (ResponseEntity<com.linkpay.userBase.model.UserForgetPasswordResponse>)null;
    try {
      userForgetPasswordResponse = userApi.userForgetPassword(userForgetPasswordRequestCopy);
    } catch(Exception e) {
      log.warn("fail to request userBase.userForgetPassword, error: [{}]", e.getMessage(), e);
      return ResultUtil.error(
          new UserForgetPasswordResponse(),
          ResponseCode.INTERNAL_REQUEST_ERROR
      );
    }

    if (!HttpStatus.OK.equals(userForgetPasswordResponse.getStatusCode())) {
      log.warn("fail to request userBase.userForgetPassword, response: [{}]", userForgetPasswordResponse);
    }

    var resultBody = userForgetPasswordResponse.getBody();
    var result = new UserForgetPasswordResponse();
    BeanUtils.copyProperties(resultBody, result);

    return result;
  }

  @Override
  public UserInfoGetResponse userInfoGet(UserInfoGetRequest userInfoGetRequest) {

    var userInfoGetRequestCopy = new com.linkpay.userBase.model.UserInfoGetRequest();
    BeanUtils.copyProperties(userInfoGetRequest, userInfoGetRequestCopy);

    var userInfoGetResponse = (ResponseEntity<com.linkpay.userBase.model.UserInfoGetResponse>)null;
    try {
      userInfoGetResponse = userApi.userInfoGet(userInfoGetRequestCopy);
    } catch(Exception e) {
      log.warn("fail to request userBase.userInfoGet, error: [{}]", e.getMessage(), e);
      return ResultUtil.error(
          new UserInfoGetResponse(),
          ResponseCode.INTERNAL_REQUEST_ERROR
      );
    }

    if (!HttpStatus.OK.equals(userInfoGetResponse.getStatusCode())
    || userInfoGetResponse.getBody() == null
    || !userInfoGetResponse.getBody().getSuccess()) {
      log.warn("fail to request userBase.userInfoGet, response: [{}]", userInfoGetResponse);
    }

    var resultBody = userInfoGetResponse.getBody();
    var result = copyUtil.copy(resultBody, UserInfoGetResponse.class);

    return result;
  }

  @Override
  public UserRegisterResponse userRegister(UserRegisterRequest userRegisterRequest) {

    var registerRequest = new com.linkpay.userBase.model.UserRegisterRequest();

    BeanUtils.copyProperties(userRegisterRequest, registerRequest);
    var registerResponse = userApi.userRegister(registerRequest);
    if (!HttpStatus.OK.equals(registerResponse.getStatusCode())) {
      log.warn("fail to request userBase.userRegister, response: [{}]", registerResponse);
    }

    var resultBody = registerResponse.getBody();
    var result = new UserRegisterResponse();
    BeanUtils.copyProperties(resultBody, result);

    if (resultBody.getData() != null) {
      var resultData = new JwtToken();
      BeanUtils.copyProperties(resultBody.getData(), resultData);
      result.setData(resultData);
    }

    return result;
  }

  @Override
  public UserResetPasswordResponse userResetPassword(Long userId,
      UserResetPasswordRequest userResetPasswordRequest) {

    var userResetPasswordRequestCopy = new com.linkpay.userBase.model.UserResetPasswordRequest();
    BeanUtils.copyProperties(userResetPasswordRequest, userResetPasswordRequestCopy);

    var userResetPasswordResponse = userApi.userResetPassword(userId, userResetPasswordRequestCopy);
    if (!HttpStatus.OK.equals(userResetPasswordResponse.getStatusCode())) {
      log.warn("fail to request userBase.userResetPassword, response: [{}]", userResetPasswordResponse);
    }

    var resultBody = userResetPasswordResponse.getBody();
    var result = new UserResetPasswordResponse();
    BeanUtils.copyProperties(resultBody, result);

    return result;
  }
}
