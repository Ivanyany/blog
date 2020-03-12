package com.ivan.blog.controller;

import com.ivan.blog.bean.Comment;
import com.ivan.blog.bean.User;
import com.ivan.blog.service.BlogService;
import com.ivan.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Ivan
 * @Date: 2020/2/26 16:24
 * @Description:
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }


    @PostMapping("/comments")
    public String addComment(Comment comment,
                             HttpSession session,
                             RedirectAttributes attributes) {

        User user = (User) session.getAttribute("user");
        Long blogId = comment.getBlog().getId();

        if (user == null || user.getType() != 1){

            attributes.addFlashAttribute("message", "非管理员身份暂时无法评论，博主正在申请前置许可，感谢您的支持^_^!");

            return "redirect:/comments/" + blogId;
        } else {

            comment.setBlog(blogService.getBlogById(blogId));
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);

            Long id = new Date().getTime();
            comment.setId(id);
            commentService.saveComment(comment);

            return "redirect:/comments/" + blogId;
        }


    }

}
