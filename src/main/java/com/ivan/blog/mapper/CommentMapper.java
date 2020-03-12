package com.ivan.blog.mapper;

import com.ivan.blog.bean.Comment;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @Auther: Ivan
 * @Date: 2020/2/26 16:31
 * @Description:
 */
public interface CommentMapper {

    @Select("select * from t_comment where blog_id = #{blogId} and parent_comment_id is null")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "blog_id", property = "blog",
                    one = @One(select = "com.ivan.blog.mapper.BlogMapper.getBlogById",fetchType= FetchType.EAGER)),
            @Result(column = "id", property = "replyComments",
                    many = @Many(select = "com.ivan.blog.mapper.CommentMapper.findReplyCommentById",fetchType= FetchType.EAGER)),
            @Result(column = "parent_comment_id", property = "parentComment",
                    one = @One(select = "com.ivan.blog.mapper.CommentMapper.findCommentById",fetchType= FetchType.EAGER))
    })
    List<Comment> findCommentByBlogId(Long blogId);

    @Insert("insert into t_comment(id, avatar, content, create_time, email, nickname, blog_id, parent_comment_id, admin_comment) " +
            "values(#{id}, #{avatar}, #{content}, #{createTime}, #{email}, #{nickname}, #{blog.id}, #{parentComment.id}, #{adminComment})")
    void saveComment(Comment comment);

    @Select("select * from t_comment where id = #{commentId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "blog_id", property = "blog",
                    one = @One(select = "com.ivan.blog.mapper.BlogMapper.getBlogById",fetchType= FetchType.EAGER)),
            @Result(column = "id", property = "replyComments",
                    many = @Many(select = "com.ivan.blog.mapper.CommentMapper.findReplyCommentById",fetchType= FetchType.EAGER)),
            @Result(column = "parent_comment_id", property = "parentComment",
                    one = @One(select = "com.ivan.blog.mapper.CommentMapper.findCommentById",fetchType= FetchType.EAGER))
    })
    Comment findCommentById(Long commentId);

    @Select("select * from t_comment where parent_comment_id = #{commentId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "blog_id", property = "blog",
                    one = @One(select = "com.ivan.blog.mapper.BlogMapper.getBlogById",fetchType= FetchType.EAGER)),
            @Result(column = "id", property = "replyComments",
                    many = @Many(select = "com.ivan.blog.mapper.CommentMapper.findReplyCommentById",fetchType= FetchType.EAGER)),
            @Result(column = "parent_comment_id", property = "parentComment",
                    one = @One(select = "com.ivan.blog.mapper.CommentMapper.findCommentById",fetchType= FetchType.EAGER))
    })
    List<Comment> findReplyCommentById(Long commentId);
}
