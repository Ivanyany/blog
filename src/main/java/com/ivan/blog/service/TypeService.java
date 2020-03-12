package com.ivan.blog.service;

import com.ivan.blog.bean.Type;
import com.ivan.blog.utils.PageInfo;

import java.util.List;

/**
 * @Auther: Ivan
 * @Date: 2020/2/1 18:15
 * @Description:
 */
public interface TypeService {

    void saveType(Type type);

    Type getTypeById(Long id);

    Type getTypeByName(String name);

    List<Type> listType();

    List<Type> listTypeTop(Integer size);

    void updateTypeById(Long id,Type type);

    void deleteTypeById(Long id);

    //分页查询
    PageInfo<Type> pageQuery(PageInfo<Type> pageInfo);
}
