package org.taskservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.taskservice.dto.TaskDto;
import org.taskservice.exceptions.AroundAspectException;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("within(@org.taskservice.aspect.annotation.ControllerLogging *)")
    public void beforeLogging(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String typeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.info("LOG BEFORE METHOD: {}.{}() {}", typeName, methodName, args);
    }

    @AfterReturning(value = "within(@org.taskservice.aspect.annotation.ControllerLogging *)",
            returning = "result")
    public void afterReturning(JoinPoint joinPoint, TaskDto result) {
        String typeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.info("LOG AFTER METHOD: {}.{}() {}", typeName, methodName, result);
    }

    @AfterThrowing(pointcut = "within(@org.taskservice.aspect.annotation.ControllerLogging *)",
            throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        String typeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.info("LOG EXCEPTION: {}.{}() {} - {} {}",
                typeName, methodName, args, exception.getClass(), exception.getMessage());
    }

    @Around("within(@org.taskservice.aspect.annotation.ControllerLogging *)")
    public Object aroundLogging(ProceedingJoinPoint joinPoint) {
        Object proceeded;
        String typeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        long start = System.currentTimeMillis();
        try {
            proceeded = joinPoint.proceed();
            log.info("LOG TIME ELAPSED OF METHOD: {}.{}() - {} ms",
                    typeName, methodName, (System.currentTimeMillis() - start));
        } catch (Throwable e) {
            throw new AroundAspectException("Exception when proceed method %s.%s()"
                    .formatted(typeName, methodName));
        }
        return proceeded;
    }
}