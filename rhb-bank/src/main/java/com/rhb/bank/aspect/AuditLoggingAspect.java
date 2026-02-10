package com.rhb.bank.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuditLoggingAspect {

    @Around("execution(* com.rhb.bank.controller..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        log.info("REQUEST → {} | args={}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());

        Object response = joinPoint.proceed();

        log.info("RESPONSE → {} | time={}ms",
                joinPoint.getSignature().toShortString(),
                System.currentTimeMillis() - start);

        return response;
    }
}
