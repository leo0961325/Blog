package com.blog.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


/**
 * 透過Component才能夠被spring boot掃描到
 */
@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    //@pointCut說明他是一個切面,會攔截所有的controller
    //域名要記得打對
    @Pointcut("execution(* com.blog.controller.*.*(..))")
    public void log(){

    }

    //切面之前使用
    //這個是Request
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        /**
         * Request的設置
         */
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String url = request.getRequestURL().toString();
        String ip  = request.getRemoteAddr();
        //獲取方法類 查joinPoint的使用
        String classMethod =joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url , ip , classMethod , args);

        logger.info("Request : {}" , requestLog);
    }

    //切面之後使用
    @After("log()")
    public void doAfter(){

//        logger.info("------------doAfter-------------");
    }

    //這是result
    @AfterReturning(returning = "result" , pointcut = "log()")
    public void doAfterReturn(Object result){

        logger.info("Result : {} " , result);
    }


    /**
     * 建立要獲取的類 信息
     */
    private class RequestLog {

        private String url ;
        private String ip ;
        private String classMethod;
        private Object[] args ;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
