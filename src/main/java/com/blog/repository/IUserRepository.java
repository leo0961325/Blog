package com.blog.repository;

import com.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository <User, Long> {


    User findByUsernameAndPassword(String username , String password);





}
