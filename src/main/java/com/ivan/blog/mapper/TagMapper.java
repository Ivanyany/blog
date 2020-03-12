package com.ivan.blog.mapper;

import com.ivan.blog.bean.Tag;
import com.ivan.blog.utils.PageInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * @Auther: Ivan
 * @Date: 2020/2/11 14:21
 * @Description:
 */
public interface TagMapper {

    //新增标签
    @Insert("insert into t_tag(id, name) values(#{id}, #{name})")
    void saveTag(Tag tag);

    //根据id查询标签
    @Select("select * from t_tag where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "blogs",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getBlogByTagId",fetchType= FetchType.EAGER))
    })
    Tag getTagById(Long id);

    //根据name查询标签
    @Select("select * from t_tag where name = #{name}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "blogs",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getBlogByTagId",fetchType= FetchType.EAGER))
    })
    Tag getTagByName(String name);

    //查询所有标签
    @Select("select * from t_tag order by id desc")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "blogs",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getBlogByTagId",fetchType= FetchType.EAGER))
    })
    List<Tag> listTag();

    @Update("update t_tag set name = #{name} where id = #{id}")
    void updateTagById(Tag tag);

    //删除数据
    @Delete("delete from t_tag where id = #{id}")
    void deleteTagById(Long id);

    //查询总数据条数
    @Select("select count(*) from t_tag")
    Long selectCountTag();

    //分页查询标签
    @Select("select * from t_tag order by id desc limit #{startNum}, #{pageSize}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "blogs",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getBlogByTagId",fetchType= FetchType.EAGER))
    })
    List<Tag> pageQuery(PageInfo<Tag> pageInfo);

    //查询前size个标签
    @Select("select * from t_tag order by id desc limit #{page},#{size}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "blogs",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getBlogByTagId",fetchType= FetchType.EAGER))
    })
    List<Tag> listTagTop(Pageable pageable);
}
