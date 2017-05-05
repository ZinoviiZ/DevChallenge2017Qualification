package com.dev.challenge.aop;

import com.dev.challenge.model.response.MessageResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
public class RestAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Before("execution(* com.dev.challenge.rest.*.*(..)) && args(request)")
    public void logRequestSupplierMethods(JoinPoint joinPoint, HttpServletRequest request) {
        logger.info("request: " + request.getMethod() + " " + request.getRequestURL());
    }

    @AfterReturning(pointcut = "execution(* com.dev.challenge.rest.*.*(..))", returning = "response")
    public void afterInvokeSupplierMethods(JoinPoint joinPoint, MessageResponse response) {
        response.setTime(sdf.format(new Date()));
    }
}