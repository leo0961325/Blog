package com.blog.exceptionHandler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * 是Controller , RestController要再寫一個
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     *  取得日誌
     *  Logger 選 org.slf4j的那一包
     */

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /** Selvet -> ModelAndView
     *  返回一個頁面和後台輸出到前端的信息
     */

    @ExceptionHandler({Exception.class})
    public ModelAndView exceptionHandler(HttpServletRequest request , Exception exception) throws Exception {

        //紀錄error 的url 跟 Exception
        logger.error("Request URL : {} , Exception : {}" ,request.getRequestURL() ,exception);

        //判斷式: 如果exception去找@Annotation -> 它會自動去找responseStatus看找不找到相對應的Exception
        if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null){
            throw exception;
        }

        ModelAndView mv = new ModelAndView();

        //前端獲取URL
        mv.addObject("url" , request.getRequestURL());

        //獲取Exception
        mv.addObject("exception" , exception);

        //設置返回的頁面 error底下的error.html
        mv.setViewName("error/error");

        return mv;
    }




}
