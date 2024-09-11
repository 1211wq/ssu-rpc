package com.ikun.example.consumer.service;

import com.ikun.example.common.model.User;
import com.ikun.example.common.service.UserService;
import com.ikun.ssurpc.springboot.starter.annotation.RpcReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConsumerService {
    @RpcReference
    private UserService userService;

    @Test
    public void consumer(){
        User user = new User();
        user.setName("ssu");
        User serviceUser = userService.getUser(user);
        System.out.println("用户名" + serviceUser.getName());
    }
}
