package com.ikun.example.common.service;

import com.ikun.example.common.model.User;

public interface UserService {

    User getUser(User user);
    default short getNumber(){
        return 1;
    }
}
