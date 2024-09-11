package com.ikun.example.common.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class User implements Serializable {
    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
