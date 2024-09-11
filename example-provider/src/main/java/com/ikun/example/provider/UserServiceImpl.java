package com.ikun.example.provider;

import com.ikun.example.common.model.User;
import com.ikun.example.common.service.UserService;

import com.ikun.ssurpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名："+user.getName());
        return user;
    }
}
