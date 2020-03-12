package com.ivan.blog.controller.admin;

import com.ivan.blog.bean.User;
import com.ivan.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Auther: Ivan
 * @Date: 2020/1/19 15:11
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到登陆页面
     * @return
     */
    @GetMapping
    public String toLoginPage() {
        return "admin/login";
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){

        User user = userService.checkUser(username, password);
        if (user != null){
            user.setPassword(null);//不要将密码保存在session
            session.setAttribute("user", user);
            return "redirect:/admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误^_^!");
            return "redirect:/admin";
        }
    }

    @GetMapping("/index")
    public String index(){
        return "admin/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
