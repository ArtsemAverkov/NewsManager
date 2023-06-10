package ru.clevertec.NewsManager.aop.loger;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Slf4j
@Aspect
@Component
public class ControllerLogAspect extends BaseAspect{


    @Pointcut("@annotation(ru.clevertec.NewsManager.aop.loger.IncludeLog)")
    public void before(){

    }
    @Pointcut("@annotation(ru.clevertec.NewsManager.aop.loger.IncludeLog)")
    public void after(){

    }

    @Before("before()")
    public void logControllersBefore(JoinPoint joinPoint){
       final HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info(BEFORE_PATTERN_CONTROLLER,
                request.getMethod(),
                joinPoint.getSignature().toShortString(),
                request.getRequestURI(),
                getArgsWhitName(joinPoint));
    }
    @AfterReturning(pointcut = "after()", returning = "result")
    public void logControllerAfter(JoinPoint joinPoint,Object result ){
       final HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info(AFTER_PATTERN_CONTROLLER,
                request.getMethod(),
                joinPoint.getSignature().toShortString(),
                request.getRequestURI(),
                getStringInstanceOf(Optional.ofNullable(result).orElse("")),
                getArgsWhitName(joinPoint));


    }
}
