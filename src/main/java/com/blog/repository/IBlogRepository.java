package com.blog.repository;


import com.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *  JpaSpecificationExecutor動態查詢組合
 */
public interface IBlogRepository extends JpaRepository<Blog,Long> , JpaSpecificationExecutor<Blog> {





















}
