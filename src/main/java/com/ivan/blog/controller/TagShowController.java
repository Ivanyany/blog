package com.ivan.blog.controller;

import com.ivan.blog.bean.Blog;
import com.ivan.blog.bean.Tag;
import com.ivan.blog.service.BlogService;
import com.ivan.blog.service.TagService;
import com.ivan.blog.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Auther: Ivan
 * @Date: 2020/2/27 15:08
 * @Description:
 */
@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    /**
     * 分页查询分类
     * @param pageInfo
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}")
    public String tags(PageInfo<Blog> pageInfo, Model model, @PathVariable Long id){

        List<Tag> tags = tagService.listTag();
        if (id == -1) {
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        //根据tagId关联查询博客
        model.addAttribute("page", blogService.pageQueryWithTagId(pageInfo, id));
        model.addAttribute("activeTagId", id);

        return "tags";
    }
}
