package com.ivan.blog.service;

import com.ivan.blog.bean.Tag;
import com.ivan.blog.utils.PageInfo;

import java.util.List;

/**
 * @Auther: Ivan
 * @Date: 2020/2/11 14:15
 * @Description:
 */
public interface TagService {

    void saveTag(Tag tag);

    Tag getTagById(Long id);

    Tag getTagByName(String name);

    List<Tag> listTag();

    List<Tag> listTag(String ids);

    void updateTagById(Long id, Tag tag);

    void deleteTagById(Long id);

    //分页查询
    PageInfo<Tag> pageQuery(PageInfo<Tag> pageInfo);

    //查询前size个标签
    List<Tag> listTagTop(Integer size);
}
