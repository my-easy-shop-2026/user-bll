package com.linkpay.userBll.service;


import com.linkpay.userBll.model.UserForgetPasswordRequest;
import com.linkpay.userBll.model.UserForgetPasswordResponse;
import com.linkpay.userBll.model.UserInfoGetRequest;
import com.linkpay.userBll.model.UserInfoGetResponse;
import com.linkpay.userBll.model.UserRegisterRequest;
import com.linkpay.userBll.model.UserRegisterResponse;
import com.linkpay.userBll.model.UserResetPasswordRequest;
import com.linkpay.userBll.model.UserResetPasswordResponse;

public interface UserService {

  UserForgetPasswordResponse userForgetPassword(
      UserForgetPasswordRequest userForgetPasswordRequest);

  UserInfoGetResponse userInfoGet(UserInfoGetRequest userInfoGetRequest);

  UserRegisterResponse userRegister(UserRegisterRequest userRegisterRequest);

  UserResetPasswordResponse userResetPassword(Long userId,
      UserResetPasswordRequest userResetPasswordRequest);

}