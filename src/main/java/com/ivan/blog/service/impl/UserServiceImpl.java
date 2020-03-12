package com.ivan.blog.service.impl;

import com.ivan.blog.bean.User;
import com.ivan.blog.mapper.UserMapper;
import com.ivan.blog.service.UserService;
import com.ivan.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: Ivan
 * @Date: 2020/1/19 15:26
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User checkUser(String username, String password) {

        User user = userMapper.checkUser(username, MD5Utils.code(password));
        return user;
    }
}
