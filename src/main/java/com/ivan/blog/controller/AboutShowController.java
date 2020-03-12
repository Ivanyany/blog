package com.ivan.blog.controller;

import com.ivan.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Auther: Ivan
 * @Date: 2020/2/27 17:17
 * @Description:
 */
@Controller
public class AboutShowController {

    @Autowired
    TypeService typeService;

    @GetMapping("/about")
    public String about(Model model) {

        //查询前6个分类
        model.addAttribute("types",typeService.listTypeTop(6));
        return "about";
    }
}
