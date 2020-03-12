package com.ivan.blog.mapper;

import com.ivan.blog.bean.Blog;
import com.ivan.blog.bean.BlogTag;
import com.ivan.blog.bean.Tag;
import com.ivan.blog.utils.PageInfo;
import com.ivan.blog.vo.BlogQuery;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Auther: Ivan
 * @Date: 2020/2/12 12:21
 * @Description:
 */
public interface BlogMapper {

    //新增博客
    @Insert("insert into t_blog(id, title, content, first_picture, flag, views, appreciation, commentabled, published, recommend, create_time, update_time, type_id, user_id, share_statement, description) " +
            "values(#{id}, #{title}, #{content}, #{firstPicture}, #{flag}, #{views}, #{appreciation}, #{commentabled}, #{published}, #{recommend}, #{createTime}, #{updateTime}, #{type.id}, #{user.id}, #{shareStatement}, #{description})")
    void saveBlog(Blog blog);

    //根据id查询博客
    @Select("select * from t_blog where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.ivan.blog.mapper.UserMapper.getUserById",fetchType= FetchType.EAGER)),
            @Result(column = "type_id", property = "type",
                    one = @One(select = "com.ivan.blog.mapper.TypeMapper.getTypeById",fetchType= FetchType.EAGER)),
            @Result(column = "id", property = "tags",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getTagsByBlogId",fetchType= FetchType.EAGER))
    })
    Blog getBlogById(Long id);

    //根据type_id查询博客
    @Select("select * from t_blog where type_id = #{typeId}")
    Blog getBlogByTypeId(Long typeId);

    //根据tag_id查询博客
    @Select("select * from t_blog where id in (select blogs_id from t_blog_tags where tags_id = #{tagId})")
    Blog getBlogByTagId(Long tagId);

    //根据title查询博客
    @Select("select * from t_blog where title = #{title}")
    Blog getBlogByName(String title);

    //查询所有博客
    @Select("select * from t_blog order by id desc")
    List<Blog> listBlog();

    @Update("update t_blog set title=#{title}, content=#{content}, first_picture=#{firstPicture}, flag=#{flag}, views=#{views}, appreciation=#{appreciation}, commentabled=#{commentabled}, published=#{published}, recommend=#{recommend}, update_time=#{updateTime}, type_id=#{type.id}, user_id=#{user.id}, share_statement=#{shareStatement}, description=#{description} where id = #{id}")
    void updateBlogById(Blog blog);

    //删除数据
    @Delete("delete from t_blog where id = #{id}")
    void deleteBlogById(Long id);

    //查询总数据条数
    @Select("select count(*) from t_blog")
    Long selectCountBlog();

    //分页查询博客
    //BlogQuery blogQ条件查询
    @Select(value = {" <script>" +
            " select * from t_blog " +
            " <where> 1=1 " +
            " <if test=\"blogQ.title != null and blogQ.title != ''\"> AND title=#{blogQ.title}</if> " +
            " <if test=\"blogQ.typeId != null\"> AND type_id=#{blogQ.typeId}</if> " +
            " <if test=\"blogQ.recommend != false\">  AND recommend=#{blogQ.recommend}</if> " +
            " </where>" +
            " order by id desc limit #{pageInfo.startNum}, #{pageInfo.pageSize}" +
            " </script>"})
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "user",
                one = @One(select = "com.ivan.blog.mapper.UserMapper.getUserById",fetchType= FetchType.EAGER)),
            @Result(column = "type_id", property = "type",
                    one = @One(select = "com.ivan.blog.mapper.TypeMapper.getTypeById",fetchType= FetchType.EAGER)),
            @Result(column = "id", property = "tags",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getTagsByBlogId",fetchType= FetchType.EAGER))
    })
    List<Blog> pageQuery(@Param(value = "pageInfo")PageInfo<Blog> pageInfo, @Param(value = "blogQ")BlogQuery blogQ);

    //根据tagId关联查询博客
    @Select("select * from t_blog where id in(select blogs_id from t_blog_tags where tags_id = #{tagId}) order by id desc limit #{pageInfo.startNum}, #{pageInfo.pageSize}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.ivan.blog.mapper.UserMapper.getUserById",fetchType= FetchType.EAGER)),
            @Result(column = "type_id", property = "type",
                    one = @One(select = "com.ivan.blog.mapper.TypeMapper.getTypeById",fetchType= FetchType.EAGER)),
            @Result(column = "id", property = "tags",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getTagsByBlogId",fetchType= FetchType.EAGER))
    })
    List<Blog> pageQueryWithTagId(@Param(value = "pageInfo")PageInfo<Blog> pageInfo, @Param(value = "tagId")Long tagId);


    //分页查询博客
    //BlogQuery blogQ条件查询
    @Select("select * from t_blog where title like #{query} or content like #{query} order by id desc limit #{pageInfo.startNum}, #{pageInfo.pageSize}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.ivan.blog.mapper.UserMapper.getUserById",fetchType= FetchType.EAGER)),
            @Result(column = "type_id", property = "type",
                    one = @One(select = "com.ivan.blog.mapper.TypeMapper.getTypeById",fetchType= FetchType.EAGER)),
            @Result(column = "id", property = "tags",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getTagsByBlogId",fetchType= FetchType.EAGER))
    })
    List<Blog> pageQueryByCondition(@Param(value = "pageInfo")PageInfo<Blog> pageInfo, @Param(value = "query")String query);

    @Insert("<script> insert into t_blog_tags(blogs_id, tags_id) values " +
                "<foreach collection='blogTagList' item='blogTag' separator=',' > " +
                    "(#{blogTag.blogId},#{blogTag.tagId})" +
                "</foreach>" +
            "</script>")
    void saveBlogAndTag(@Param(value = "blogTagList") List<BlogTag> blogTagList);

    @Select("select * from t_tag where id in (select tags_id from t_blog_tags where blogs_id = #{id})")
    List<Tag> getTagsByBlogId(Long id);

    @Delete("delete from t_blog_tags where blogs_id = #{id}")
    void deleteBlogTagByBlogId(Long id);

    //查询前size个推荐博客
    @Select("select * from t_blog where recommend = 1 order by id desc limit #{page},#{size}")
    List<Blog> listBlogTop(Pageable pageable);

    //更新浏览次数
    @Update("update t_blog set views = views + 1 where id = #{id}")
    void updateViews(Long id);

    //查询博客所属年份
    @Select("select date_format(update_time,'%Y') AS year from t_blog group by year")
    List<String> findGroupYear();

    //根据博客所属年份查询博客
    @Select("select * from t_blog where date_format(update_time,'%Y') = #{year} order by id desc")
    List<Blog> findBlogByYear(String year);

    //更新3条最新博客
    @Select("select * from t_blog where recommend = 1 order by id desc limit #{size}")
    List<Blog> listRecommendBlogTop(Integer size);
}
