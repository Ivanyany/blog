package com.ivan.blog.controller;

import com.ivan.blog.bean.Blog;
import com.ivan.blog.bean.Type;
import com.ivan.blog.service.BlogService;
import com.ivan.blog.service.TypeService;
import com.ivan.blog.utils.PageInfo;
import com.ivan.blog.vo.BlogQuery;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    /**
     * 分页查询分类
     * @param pageInfo
     * @param model
     * @return
     */
    @GetMapping("/types/{id}")
    public String types(PageInfo<Blog> pageInfo, Model model, @PathVariable Long id){

        List<Type> types = typeService.listType();
        if (id == -1) {
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types", types);
        model.addAttribute("page", blogService.pageQuery(pageInfo, blogQuery));
        model.addAttribute("activeTypeId", id);

        return "types";
    }
}
