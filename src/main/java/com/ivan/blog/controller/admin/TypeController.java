package com.ivan.blog.controller.admin;

import com.ivan.blog.bean.Type;
import com.ivan.blog.service.TypeService;
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
 * @Date: 2020/2/1 18:37
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    TypeService typeService;

    /**
     * 分页查询分类
     * @param pageInfo
     * @param model
     * @return
     */
    @GetMapping("/types")
    public String types(PageInfo<Type> pageInfo, Model model){

        PageInfo<Type> pageInfoResult = typeService.pageQuery(pageInfo);
        model.addAttribute("page", pageInfoResult);

        return "admin/types";
    }

    /**
     * 跳转到分类新增页面
     * @return
     */
    @GetMapping("/types/input")
    public String toAddType(Model model){
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    /**
     * 跳转到分类修改页面
     * @return
     */
    @GetMapping("/types/{id}/input")
    public String toEditType(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getTypeById(id));
        return "admin/types-input";
    }


    /**
     * 新增分类
     * @param type 用于前段校验
     * @param result 校验结果
     * @param attributes
     * @return
     */
    @PostMapping("/types/input")
    public String addType(@Valid Type type, BindingResult result, RedirectAttributes attributes){

        //名称不能重复校验
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name", "nameError", "不能添加重复的分类名");
        }

        //校验
        if (result.hasErrors()){
            return "admin/types-input";
        }

        Long id = new Date().getTime();
        type.setId(id);
        typeService.saveType(type);

        Type resultType = typeService.getTypeById(id);
        if (resultType == null){
            attributes.addFlashAttribute("message","新增失败");
        } else {
            attributes.addFlashAttribute("message","新增成功");
        }

        return "redirect:/admin/types";
    }

    /**
     * 修改分类
     * @param type
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/types/{id}")
    public String editType(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes){

        //名称不能重复校验
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name", "nameError", "不能添加重复的分类名");
        }

        //校验
        if (result.hasErrors()){
            return "admin/types-input";
        }

        //修改
        typeService.updateTypeById(id, type);

        Type resultType = typeService.getTypeById(id);
        if (resultType == null){
            attributes.addFlashAttribute("message","修改失败");
        } else {
            attributes.addFlashAttribute("message","修改成功");
        }

        return "redirect:/admin/types";
    }

    /**
     * 删除分类
     * @return
     */
    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable Long id){
        typeService.deleteTypeById(id);
        return "redirect:/admin/types";
    }


}
