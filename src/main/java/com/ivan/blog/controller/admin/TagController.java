package com.ivan.blog.controller.admin;

import com.ivan.blog.bean.Tag;
import com.ivan.blog.service.TagService;
import com.ivan.blog.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;

/**
 * @Auther: Ivan
 * @Date: 2020/2/1 20:43
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    TagService tagService;

    /**
     * 分页查询标签
     * @param pageInfo
     * @param model
     * @return
     */
    @GetMapping("/tags")
    public String tags(PageInfo<Tag> pageInfo, Model model){

        PageInfo<Tag> pageInfoResult = tagService.pageQuery(pageInfo);
        model.addAttribute("page", pageInfoResult);

        return "admin/tags";
    }

    /**
     * 跳转到标签新增页面
     * @return
     */
    @GetMapping("/tags/input")
    public String toAddTag(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    /**
     * 跳转到标签修改页面
     * @return
     */
    @GetMapping("/tags/{id}/input")
    public String toEditTag(@PathVariable Long id, Model model){
        model.addAttribute("tag",tagService.getTagById(id));
        return "admin/tags-input";
    }

    /**
     * 新增标签
     * @param tag 用于前段校验
     * @param result 校验结果
     * @param attributes
     * @return
     */
    @PostMapping("/tags/input")
    public String addTag(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){

        //名称不能重复校验
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null){
            result.rejectValue("name", "nameError", "不能添加重复的标签名");
        }

        //校验
        if (result.hasErrors()){
            return "admin/tags-input";
        }

        Long id = new Date().getTime();
        tag.setId(id);
        tagService.saveTag(tag);

        Tag resultTag = tagService.getTagById(id);
        if (resultTag == null){
            attributes.addFlashAttribute("message","新增失败");
        } else {
            attributes.addFlashAttribute("message","新增成功");
        }

        return "redirect:/admin/tags";
    }

    /**
     * 修改标签
     * @param tag
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String editTag(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes){

        //名称不能重复校验
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null){
            result.rejectValue("name", "nameError", "不能添加重复的分类名");
        }

        //校验
        if (result.hasErrors()){
            return "admin/tags-input";
        }

        //修改
        tagService.updateTagById(id, tag);

        Tag resultTag = tagService.getTagById(id);
        if (resultTag == null){
            attributes.addFlashAttribute("message","修改失败");
        } else {
            attributes.addFlashAttribute("message","修改成功");
        }

        return "redirect:/admin/tags";
    }

    /**
     * 删除标签
     * @return
     */
    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable Long id){
        tagService.deleteTagById(id);
        return "redirect:/admin/tags";
    }
}
