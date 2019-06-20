package com.common.admin.common;

import com.alibaba.fastjson.JSONObject;
import com.common.admin.utils.LogUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: liujianqiang
 * @Date: 2019-03-20
 * @Description:
 */
@Aspect
@Component
public class RequestAspect {
    @Pointcut("execution(public * com.common.admin.controller..*.*(..))")
    public void webLog(){
    }
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //设置请求追踪
        LogUtil.setUUID();
        // 记录下请求内容
       LogUtil.info("URL : " + request.getRequestURL().toString()+" =====> 入参 = " +JSONObject.toJSONString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
    }
}