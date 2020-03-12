package com.ivan.blog.mapper;

import com.ivan.blog.bean.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Auther: Ivan
 * @Date: 2020/1/19 15:28
 * @Description:
 */
public interface UserMapper {
    @Select("select * from t_user where username = #{username} and password = #{password}")
    User checkUser(@Param("username") String username, @Param("password") String password);

    @Select("select * from t_user where id = #{id}")
    User getUserById(Long id);
}
