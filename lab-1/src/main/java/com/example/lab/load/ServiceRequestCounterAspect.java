package com.example.lab.load;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.example.lab.service.RequestCounterService;

@Aspect
@Component
public class ServiceRequestCounterAspect {
    @Pointcut(value = "execution(public * com.example.lab.service.*.*(..))")
    private void servicesPublicMethods() {}

    @Before(value = "servicesPublicMethods()")
    public void countRequests(JoinPoint joinPoint) {
        RequestCounterService.incrementCountr();
    }
}
