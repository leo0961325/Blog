package com.blog.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登入攔截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    //A+I Chose preHandle
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        //如果Session沒有拿到東西就重定向到admin
        if (request.getSession().getAttribute("user") == null){

            response.sendRedirect("/admin");
            return false;
        }

        return true;
    }
}
