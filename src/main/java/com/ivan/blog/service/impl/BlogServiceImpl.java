package com.ivan.blog.service.impl;

import com.ivan.blog.exception.NotFoundException;
import com.ivan.blog.bean.Blog;
import com.ivan.blog.bean.BlogTag;
import com.ivan.blog.bean.Tag;
import com.ivan.blog.mapper.BlogMapper;
import com.ivan.blog.service.BlogService;
import com.ivan.blog.utils.MarkdownUtils;
import com.ivan.blog.utils.MyBeanUtils;
import com.ivan.blog.utils.PageInfo;
import com.ivan.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Auther: Ivan
 * @Date: 2020/2/1 18:20
 * @Description:
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogMapper blogMapper;

    @Transactional
    @Override
    public void saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);

        //保存博客和标签
        saveBlogTag(blog);

        //保存博客
        blogMapper.saveBlog(blog);
    }

    //保存博客和标签
    private void saveBlogTag(Blog blog){
        List<BlogTag> blogTagList = new ArrayList<>();
        for(Tag tag : blog.getTags()){
            blogTagList.add(new BlogTag(blog.getId(), tag.getId()));
        }

        //保存博客和标签
        blogMapper.saveBlogAndTag(blogTagList);
    }

    @Transactional
    @Override
    public Blog getBlogById(Long id) {
        return blogMapper.getBlogById(id);
    }

    @Transactional
    @Override
    public Blog getBlogByName(String name) {
        return blogMapper.getBlogByName(name);
    }

    @Transactional
    @Override
    public List<Blog> listBlog() {
        return blogMapper.listBlog();
    }

    @Transactional
    @Override
    public void updateBlogById(Long id, Blog blog) {

        Blog b = blogMapper.getBlogById(id);
        if (b == null){
            throw new NotFoundException("不存在该博客");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        //设置更新时间
        b.setUpdateTime(new Date());

        //删除BlogTag
        blogMapper.deleteBlogTagByBlogId(b.getId());

        //重新保存BlogTag
        saveBlogTag(b);

        //更新blog
        blogMapper.updateBlogById(b);
    }

    @Transactional
    @Override
    public void deleteBlogById(Long id) {
        //删除blog_tag
        blogMapper.deleteBlogTagByBlogId(id);
        //删除blog
        blogMapper.deleteBlogById(id);
    }

    //分页查询
    @Override
    public PageInfo<Blog> pageQuery(PageInfo<Blog> pageInfo, BlogQuery blogQ) {

        //结果集
        pageQuery(pageInfo).setContent(blogMapper.pageQuery(pageInfo, blogQ));

        return pageInfo;
    }

    //根据tagId关联查询博客
    @Override
    public PageInfo<Blog> pageQueryWithTagId(PageInfo<Blog> pageInfo, Long id) {
        //结果集
        pageQuery(pageInfo).setContent(blogMapper.pageQueryWithTagId(pageInfo, id));

        return pageInfo;
    }

    private PageInfo<Blog> pageQuery(PageInfo<Blog> pageInfo){
        //当前是第几页,默认查询第1页
        Long pageNum =pageInfo.getPageNum() == null ? 1 : pageInfo.getPageNum();
        pageInfo.setPageNum(pageNum);

        //每页的数据量,默认每页5行数据
        Long pageSize = pageInfo.getPageSize() == null ? 5 : pageInfo.getPageSize();
        pageInfo.setPageSize(pageSize);

        //从第几个数据开始查
        Long startNum = (pageInfo.getPageNum()-1)*pageInfo.getPageSize();
        pageInfo.setStartNum(startNum);

        //总记录数
        Long total = blogMapper.selectCountBlog();
        pageInfo.setTotal(total);

        //总页数
        Long pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        pageInfo.setPages(pages);

        //是否是第一页
        if(pageNum == 1){
            pageInfo.setFirst(true);
        }

        //是否是最后一页
        if(pageNum == pages){
            pageInfo.setLast(true);
        }

        return pageInfo;
    }

    @Override
    public List<Blog> listBlogTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return blogMapper.listBlogTop(pageable);
    }

    /**
     * 查询
     * @param query 查询条件
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo<Blog> listBlogSearch(String query, PageInfo<Blog> pageInfo) {

        //结果集
        pageQuery(pageInfo).setContent(blogMapper.pageQueryByCondition(pageInfo, query));

        return pageInfo;
    }

    /**
     * 获取blog并转成HTML代码
     * @param id
     * @return
     */
    @Override
    public Blog getAndConvert(Long id) {

        //更新浏览次数+1
        blogMapper.updateViews(id);

        Blog blog = blogMapper.getBlogById(id);
        if (blog == null){
            throw new NotFoundException("不存在该博客");
        }

        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);

        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        return b;
    }

    //查询博客总条数
    @Override
    public Long countBlog() {
        return blogMapper.selectCountBlog();
    }

    //博客归档
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        //查询博客所属年份
        List<String> years = blogMapper.findGroupYear();
        Map<String, List<Blog>> map = new TreeMap<>(
                Comparator.reverseOrder());
        for (String year : years) {
            //根据博客所属年份查询博客
            map.put(year, blogMapper.findBlogByYear(year));
        }
        return map;
    }

    //更新3条最新博客
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {

        return blogMapper.listRecommendBlogTop(size);
    }
}
