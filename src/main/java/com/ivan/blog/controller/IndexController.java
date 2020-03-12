package com.ivan.blog.controller;

import com.ivan.blog.bean.Blog;
import com.ivan.blog.bean.Tag;
import com.ivan.blog.bean.Type;
import com.ivan.blog.service.BlogService;
import com.ivan.blog.service.TagService;
import com.ivan.blog.service.TypeService;
import com.ivan.blog.utils.PageInfo;
import com.ivan.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther: Ivan
 * @Date: 2020/1/18 14:56
 * @Description:
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    /**
     * 首页查询展示
     * @param pageInfo
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(PageInfo<Blog> pageInfo, Model model) {
        //不用条件查询,查询条件设为空即可
        BlogQuery blogQuery = new BlogQuery();
        model.addAttribute("page",blogService.pageQuery(pageInfo, blogQuery));
        List<Type> types = typeService.listTypeTop(6);
        //查询前6个分类
        model.addAttribute("types",typeService.listTypeTop(6));
        //查询前10个标签
        List<Tag> tags = tagService.listTagTop(10);
        model.addAttribute("tags",tagService.listTagTop(10));

        //查询前8个推荐博客(按时间排序)
        List<Blog> recommendBlogs = blogService.listBlogTop(8);
        model.addAttribute("recommendBlogs",blogService.listBlogTop(8));

        return "index";
    }

    /**
     * 全局搜索
     * @param pageInfo
     * @param query
     * @param model
     * @return
     */
    @PostMapping("/search")
    public String search(PageInfo<Blog> pageInfo, @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.listBlogSearch("%"+query+"%", pageInfo));
        model.addAttribute("query", query);
         return "search";
    }

    /**
     * 查询给定博客
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        //model.addAttribute("blog",blogService.getBlogById(id));
        return "blog";
    }

    /**
     * 更新3条最新博客
     * @param model
     * @return
     */
    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

}
