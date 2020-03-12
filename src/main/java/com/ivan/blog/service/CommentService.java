package com.ivan.blog.service;

import com.ivan.blog.bean.Comment;

import java.util.List;

/**
 * @Auther: Ivan
 * @Date: 2020/2/26 16:28
 * @Description:
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
