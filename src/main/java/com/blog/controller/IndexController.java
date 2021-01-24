package com.blog.controller;


import com.blog.exception.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 *  Create By Harvey 2021/01/13
 *  要加thymeleaf-3.0.9那包
 *  pom <property> </property> 不用再加themeleaf的怪東西
 */

@Controller
public class IndexController {

    @GetMapping(value = "/")
    public String index(){

        //錯誤的範例使用
        //int x = 9/0;

//        String blog = null;
//        if (blog == null){
//
//            throw new NotFoundException("Blog not exist");
//        }

        System.out.println("------------index-----------");
        //如果是資料夾下要在加 dir/index
        return "index";
    }

    @GetMapping(value = "/blog")
    public String blog(){

        //錯誤的範例使用
        //int x = 9/0;

//        String blog = null;
//        if (blog == null){
//
//            throw new NotFoundException("Blog not exist");
//        }

        return "blog";
    }





}
