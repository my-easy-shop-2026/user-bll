package com.linkpay.userBll.controller;

import com.linkpay.commonModule.constant.ResponseCode;
import com.linkpay.commonModule.exception.NoPermissionException;
import com.linkpay.commonModule.exception.NoWebRequestException;
import com.linkpay.commonModule.jwt.JwtTokenHeaderKey;
import com.linkpay.commonModule.jwt.JwtUtil;
import com.linkpay.commonModule.result.ResultUtil;
import com.linkpay.userBase.api.UserApi;
import com.linkpay.userBll.api.UserApiDelegate;
import com.linkpay.userBll.model.UserForgetPasswordRequest;
import com.linkpay.userBll.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.defs.NotEmptyDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;


@Component
@Slf4j
public class UserApiControllerImpl implements UserApiDelegate {

    @Autowired
    UserService userService;

    @Autowired
    UserApi userApi;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private NativeWebRequest nativeWebRequest;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

//    @Override
//    public ResponseEntity<com.linkpay.userBll.model.UserGetByIdResponse> userGetById(Long userId,
//                                                                              com.linkpay.userBll.model.User user) {
//
//        var request = getRequest().orElseThrow(() -> new NoWebRequestException("no web request"));
//
//        var validateResult = jwtUtil.validateAccessToken(request.getHeader(JwtTokenHeaderKey.ACCESS_TOKEN));
//        if (!validateResult.getSuccess()) {
//            log.info("jwt validate failed: {}", validateResult);
//            throw new NoPermissionException();
//        }
//
//        if (validateResult.getData() != null) {
//            log.error("jwt validate result no data: {}", validateResult);
//            throw new NoPermissionException();
//        }
//
//        var claims = validateResult.getData();
//
//        var jwt = jwtUtil.jwt().importClaims(claims);
//        if (!userId.equals(jwt.getSubject())) {
//            log.info("jwt validate failed: illegal userid");
//            throw new NoPermissionException();
//        }
//
//        var userCopy = new com.linkpay.userBase.model.User();
//        BeanUtils.copyProperties(user, userCopy);
//
//        var result = userApi.userGetById(userId, userCopy);
//        var resultCopy = new ResponseEntity<com.linkpay.userBll.model.UserGetByIdResponse>(OK);
//        BeanUtils.copyProperties(result, resultCopy);
//        return resultCopy;
//    }

    @Override
    public ResponseEntity<com.linkpay.userBll.model.UserForgetPasswordResponse> userForgetPassword(
        UserForgetPasswordRequest userForgetPasswordRequest) {

        HibernateValidatorConfiguration configuration = Validation
            .byProvider(HibernateValidator.class)
            .configure();

        ConstraintMapping constraintMapping = configuration.createConstraintMapping();

        constraintMapping
            .type(com.linkpay.userBll.model.UserForgetPasswordRequest.class)
            .field("email").constraint(new NotEmptyDef())
            .field("emailVerificationCode").constraint(new NotEmptyDef())
            .field("password").constraint(new NotEmptyDef())
            .field("confirmPassword").constraint(new NotEmptyDef());

        Validator validator = configuration
            .addMapping(constraintMapping)
            .buildValidatorFactory()
            .getValidator();

        Set<ConstraintViolation<com.linkpay.userBll.model.UserForgetPasswordRequest>> constraintViolations = validator.validate(
            userForgetPasswordRequest);

        if (!constraintViolations.isEmpty()) {
            var message = constraintViolations.stream().map(e -> e.getPropertyPath() + e.getMessage())
                .collect(Collectors.joining(","));
            return new ResponseEntity<>(
                ResultUtil.error(
                    new com.linkpay.userBll.model.UserForgetPasswordResponse(),
                    ResponseCode.INVALID_PARAMETER,
                    message),
                HttpStatus.OK);
        }

        return new ResponseEntity<>(userService.userForgetPassword(userForgetPasswordRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<com.linkpay.userBll.model.UserRegisterResponse> userRegister(com.linkpay.userBll.model.UserRegisterRequest userRegisterRequest) {

        HibernateValidatorConfiguration configuration = Validation
            .byProvider(HibernateValidator.class)
            .configure();

        ConstraintMapping constraintMapping = configuration.createConstraintMapping();

        constraintMapping
            .type(com.linkpay.userBll.model.UserRegisterRequest.class);
//            .field("userId").constraint(new NotNullDef())
//            .field("userFirstName").constraint(new NotEmptyDef())
//            .field("userLastName").constraint(new NotEmptyDef())
//            .field("userGender").constraint(new NotNullDef())
//            .field("userCountryCode").constraint(new NotNullDef())
//            .field("userNationalPhoneNumber").constraint(new NotEmptyDef())
//            .field("orderAmount").constraint(new NotNullDef())
//            .field("shippingCity").constraint(new NotEmptyDef())
//            .field("shippingNationCode").constraint(new NotNullDef())
//            .field("shippingPostalCode").constraint(new NotEmptyDef())
//            .field("shippingAddress").constraint(new NotEmptyDef())
//            .field("cryptocurrency").constraint(new NotEmptyDef());

        Validator validator = configuration
            .addMapping(constraintMapping)
            .buildValidatorFactory()
            .getValidator();

        Set<ConstraintViolation<com.linkpay.userBll.model.UserRegisterRequest>> constraintViolations = validator.validate(
            userRegisterRequest);

        if (!constraintViolations.isEmpty()) {
            var message = constraintViolations.stream().map(e -> e.getPropertyPath() + e.getMessage())
                .collect(Collectors.joining(","));
            return new ResponseEntity<>(
                ResultUtil.error(
                    new com.linkpay.userBll.model.UserRegisterResponse(),
                    ResponseCode.INVALID_PARAMETER,
                    message),
                HttpStatus.OK);
        }

        return new ResponseEntity<>(userService.userRegister(userRegisterRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<com.linkpay.userBll.model.UserInfoGetResponse> userInfoGet(com.linkpay.userBll.model.UserInfoGetRequest userInfoGetRequest) {

        var testAuth = SecurityContextHolder.getContext().getAuthentication();
        log.info("test {}", testAuth.getPrincipal());

        var request = getRequest();
        var authorizationHeader = request
            .orElseThrow(() -> new NoWebRequestException("no web request"))
            .getHeader(JwtTokenHeaderKey.AUTHORIZATION);;
        var accessToken = (String)null;

        if(authorizationHeader != null && authorizationHeader.startsWith(JwtTokenHeaderKey.BEARER_PREFIX)){
            accessToken = authorizationHeader.substring(7);
        } else {
            log.info("no permission. authorization: {}", authorizationHeader);
            throw new NoPermissionException();
        }

        var validateResult = jwtUtil.validateAccessToken(accessToken);
        if (!validateResult.getSuccess()) {
            log.info("jwt validate failed: {}", validateResult);
            throw new NoPermissionException();
        }

        if (validateResult.getData() == null) {
            log.error("jwt validate result no data: {}", validateResult);
            throw new NoPermissionException();
        }

        var claims = validateResult.getData();

        var jwt = jwtUtil.jwt().importClaims(claims);
        var userId = jwt.getSubject().orElseThrow(() -> {
            log.error("jwt does not have subject, {}", jwt);
            return new NoPermissionException();
        });

        userInfoGetRequest.setId(Long.parseLong(userId));

        return new ResponseEntity<>(userService.userInfoGet(userInfoGetRequest), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<com.linkpay.userBll.model.UserResetPasswordResponse> userResetPassword(Long userId,
        com.linkpay.userBll.model.UserResetPasswordRequest userResetPasswordRequest) {

        var request = getRequest();
        var authorizationHeader = request
            .orElseThrow(() -> new NoWebRequestException("no web request"))
            .getHeader(JwtTokenHeaderKey.AUTHORIZATION);;
        var accessToken = (String)null;

        if(authorizationHeader != null && authorizationHeader.startsWith(JwtTokenHeaderKey.BEARER_PREFIX)){
            accessToken = authorizationHeader.substring(7);
        } else {
            log.info("no permission. authorization: {}", authorizationHeader);
            throw new NoPermissionException();
        }

        var validateResult = jwtUtil.validateAccessToken(accessToken);
        if (!validateResult.getSuccess()) {
            log.info("jwt validate failed: {}", validateResult);
            throw new NoPermissionException();
        }

        if (validateResult.getData() == null) {
            log.error("jwt validate result no data: {}", validateResult);
            throw new NoPermissionException();
        }

        var claims = validateResult.getData();

        var jwt = jwtUtil.jwt().importClaims(claims);
        if (!userId.equals(Long.parseLong(jwt.getSubject().orElseThrow(() -> {
            log.error("jwt does not have subject, {}", jwt);
            return new NoPermissionException();
        })))) {
            log.info("user id [{}] not the same as access token [{}]", userId, jwt);
            throw new NoPermissionException();
        }
        userId = Long.parseLong(jwt.getSubject().orElseThrow(() -> {
            log.error("jwt does not have subject, {}", jwt);
            return new NoPermissionException();
        }));



        return new ResponseEntity<>(userService.userResetPassword(userId, userResetPasswordRequest), HttpStatus.OK);

    }
}