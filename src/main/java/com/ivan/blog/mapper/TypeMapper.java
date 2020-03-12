package com.ivan.blog.mapper;

import com.ivan.blog.bean.Type;
import com.ivan.blog.utils.PageInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Auther: Ivan
 * @Date: 2020/2/1 18:21
 * @Description:
 */
public interface TypeMapper {

    //新增分类
    @Insert("insert into t_type(id, name) values(#{id}, #{name})")
    void saveType(Type type);

    //根据id查询分类
    @Select("select * from t_type where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "blogs",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getBlogByTypeId",fetchType= FetchType.EAGER))
    })
    Type getTypeById(Long id);

    //根据name查询分类
    @Select("select * from t_type where name = #{name}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "blogs",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getBlogByTypeId",fetchType= FetchType.EAGER))
    })
    Type getTypeByName(String name);

    //查询所有分类
    @Select("select * from t_type order by id desc")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "blogs",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getBlogByTypeId",fetchType= FetchType.EAGER))
    })
    List<Type> listType();

    @Update("update t_type set name = #{name} where id = #{id}")
    void updateTypeById(Type type);

    //删除数据
    @Delete("delete from t_type where id = #{id}")
    void deleteTypeById(Long id);

    //查询总数据条数
    @Select("select count(*) from t_type")
    Long selectCountType();

    //分页查询分类
    @Select("select * from t_type order by id desc limit #{startNum}, #{pageSize}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "blogs",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getBlogByTypeId",fetchType= FetchType.EAGER))
    })
    List<Type> pageQuery(PageInfo<Type> pageInfo);

    //查询前size个分类
    @Select("select * from t_type order by id desc limit #{page},#{size}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "blogs",
                    many = @Many(select = "com.ivan.blog.mapper.BlogMapper.getBlogByTypeId",fetchType= FetchType.EAGER))
    })
    List<Type> listTypeTop(Pageable pageable);
}
