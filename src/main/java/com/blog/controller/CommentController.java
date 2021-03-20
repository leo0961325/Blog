package com.blog.controller;


import com.blog.model.Comment;
import com.blog.model.User;
import com.blog.service.IBlogService;
import com.blog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {


    @Autowired
    ICommentService iCommentService;

    @Autowired
    IBlogService iBlogService;

    //先把大頭貼在property寫死
    @Value("${comment.avatar}")
    private String avatar;


    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){

        model.addAttribute("comments" , iCommentService.listCommentByBlogId(blogId));

        //返回Blog頁面下的commentList，局部刷新commentList
        return "blog :: commentList";
    }


    @PostMapping("/comments")
    public String post(Comment comment , HttpSession session){

        Long blogId = comment.getBlog().getId();

        comment.setBlog(iBlogService.getBlog(blogId));


        //判斷是否為管理員，並透過session拿到管理員資料
        User user = (User) session.getAttribute("user");
        //如果為管理員
        if(user != null){
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);//管理員評論設置，對應blog th
        }
        else {
            comment.setAvatar(avatar);
        }


        iCommentService.saveComment(comment);

        return "redirect:/comments/" + blogId ;
    }








}
