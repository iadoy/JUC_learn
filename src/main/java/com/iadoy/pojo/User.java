package com.iadoy.pojo;

import lombok.Data;

@Data
public class User {
    String uid; // 用户ID
    String nickName; //昵称
    public volatile int age; //年龄

    public User(String uid, String nickName) {
        this.uid = uid;
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "User{" + "uid='" + getUid() + '\'' +
                ", nickName='" + getNickName() + '\'' +
                ", age=" + getAge() + '}';
    }
}
