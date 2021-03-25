package com.blog.service;


import com.blog.model.Blog;
import com.blog.vo.BlogQuery;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IBlogService {




    Blog getBlog(Long id);

    Blog getAndConvert(Long id) throws NotFoundException;


    Page<Blog> listBlog(Pageable pageable , BlogQuery blog );

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long tagId , Pageable pageable);

    Page<Blog> listBlog(String query , Pageable pageable);

    List<Blog> listRecommendBlog(Integer size);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id , Blog blog) throws NotFoundException;

    void deleteBlog(Long id) throws NotFoundException;












}
