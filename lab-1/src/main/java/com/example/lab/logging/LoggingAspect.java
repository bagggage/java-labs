package com.example.lab.logging;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.lab.exceptions.ApiException;

@Aspect
@Component
public class LoggingAspect {
    private static Map<String, Logger> loggersMap = new HashMap<>();

    private Logger getClassLogger(@SuppressWarnings("rawtypes") Class clazz) {
        if (loggersMap.containsKey(clazz.getSimpleName())) {
            return loggersMap.get(clazz.getSimpleName());
        }
        else {
            Logger logger = LoggerFactory.getLogger(clazz);
            loggersMap.put(clazz.getSimpleName(), logger);

            return logger;
        }
    }

    private String argsToString(Object[] args) {
        String result = new String();

        for (int i = 0; i < args.length; ++i) {
            if (args[i] == null) {
                result += "null";
            }
            else if (args[i] instanceof String) {
                result += '\"' + args[i].toString() + '\"';
            }
            else {
                result += args[i].toString();
            }
            
            if (i < args.length - 1) result += ", ";
        }

        return result;
    }

    @Pointcut(value = "execution(public * com.example.lab.controller.*.*(..))")
    private void controllersPublicMethods() {}

    @Before(value = "controllersPublicMethods()")
    public void executedMethodsLog(JoinPoint joinPoint) { 
        Logger logger = getClassLogger(joinPoint.getTarget().getClass());
        logger.info("Request on {}({})", joinPoint.getSignature().getName(), argsToString(joinPoint.getArgs()));
    }

    @AfterThrowing(value = "controllersPublicMethods()", throwing = "exception")
    public void exceptionsLog(JoinPoint joinPoint, Throwable exception) {
        if (!(exception instanceof ApiException)) return;

        Logger logger = getClassLogger(joinPoint.getTarget().getClass());

        if (exception.getMessage() != null) {
            logger.warn("{} at {}(...): {}",
                        exception.getClass().getSimpleName(),
                        joinPoint.getSignature().getName(),
                        exception.getMessage());
        }
        else {
            logger.warn("{} at {}(...)",
                        exception.getClass().getSimpleName(),
                        joinPoint.getSignature().getName());
        }
    }
}