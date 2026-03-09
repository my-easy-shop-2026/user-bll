package com.linkpay.userBll.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private ObjectMapper mapper;

    @Pointcut("bean(*Controller)") // 抓取所有Controller，可依實際需求調整
    public void logPointCut() { }

    @Before("logPointCut()")
    public void logging(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String argStr = Arrays.toString(joinPoint.getArgs());
        log.info("请求信息：, url={{}}, method={{}}, ip={{}}, class_method={{}}, args={{}}", request.getRequestURL(), request.getMethod(),
                request.getHeader("Proxy-Client-IP"), joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),argStr);

    }

    @AfterReturning(pointcut = "logPointCut()",returning = "object")//打印输出结果
    public void doAfterReturning(Object object){
        log.info("response={}", object);
    }
}