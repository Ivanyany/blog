package com.ivan.blog.service;

import com.ivan.blog.bean.Blog;
import com.ivan.blog.utils.PageInfo;
import com.ivan.blog.vo.BlogQuery;

import java.util.List;
import java.util.Map;

/**
 * @Auther: Ivan
 * @Date: 2020/2/12 12:15
 * @Description:
 */
public interface BlogService {

    void saveBlog(Blog blog);

    Blog getBlogById(Long id);

    Blog getBlogByName(String name);

    List<Blog> listBlog();

    void updateBlogById(Long id, Blog blog);

    void deleteBlogById(Long id);

    //分页查询
    PageInfo<Blog> pageQuery(PageInfo<Blog> pageInfo, BlogQuery blogQ);

    //根据tagId关联查询博客
    PageInfo<Blog> pageQueryWithTagId(PageInfo<Blog> pageInfo, Long id);

    //查询前size个推荐博客
    List<Blog> listBlogTop(Integer size);

    /**
     * 查询
     * @param query 查询条件
     * @param pageInfo
     * @return
     */
    PageInfo<Blog> listBlogSearch(String query, PageInfo<Blog> pageInfo);

    /**
     * 获取blog并转成HTML代码
     * @param id
     * @return
     */
    Blog getAndConvert(Long id);

    //查询博客总条数
    Long countBlog();

    Map<String, List<Blog>> archiveBlog();

    //更新3条最新博客
    List<Blog> listRecommendBlogTop(Integer size);
}
