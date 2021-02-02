package com.blog.service.IMP;

import com.blog.model.User;
import com.blog.repository.IUserRepository;
import com.blog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {



    @Autowired
    IUserRepository iUserRepository;


    @Override
    public User checkUser(String username, String password) {

        User user = iUserRepository.findByUsernameAndPassword(username,password);




        return user;
    }
}
