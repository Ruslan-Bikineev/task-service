package org.taskservice.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.taskservice.exceptions.AroundAspectException;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("within(@org.taskservice.aspect.annotation.ControllerLogging *)")
    public void beforeLogging(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String typeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        logger.info("LOG BEFORE METHOD: {}.{}() {}", typeName, methodName, args);
    }

    @AfterReturning("within(@org.taskservice.aspect.annotation.ControllerLogging *)")
    public void afterReturning(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String typeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        logger.info("LOG AFTER METHOD: {}.{}() {}", typeName, methodName, args);
    }

    @AfterThrowing(pointcut = "within(@org.taskservice.aspect.annotation.ControllerLogging *)",
            throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        String typeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        logger.info("LOG EXCEPTION: {}.{}() {} - {} {}",
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
        } catch (Throwable e) {
            throw new AroundAspectException("Exception when proceed method %s.%s()"
                    .formatted(typeName, methodName));
        }
        logger.info("LOG TIME ELAPSED OF METHOD: {}.{}() - {} ms",
                typeName, methodName, (System.currentTimeMillis() - start));
        return proceeded;
    }
}