package com.ivan.blog.service;

import com.ivan.blog.bean.User;

/**
 * @Auther: Ivan
 * @Date: 2020/1/19 15:26
 * @Description:
 */
public interface UserService {

    User checkUser(String username, String password);
}
