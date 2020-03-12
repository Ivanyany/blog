package com.ivan.blog.controller.admin;

import com.ivan.blog.bean.Blog;
import com.ivan.blog.bean.User;
import com.ivan.blog.service.BlogService;
import com.ivan.blog.service.TagService;
import com.ivan.blog.service.TypeService;
import com.ivan.blog.utils.CommonFileUtil;
import com.ivan.blog.utils.PageInfo;
import com.ivan.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: Ivan
 * @Date: 2020/1/19 16:52
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class BlogController {


    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @Autowired
    private CommonFileUtil commonFileUtil;
    @Value("${server.url}")
    private String serverUrl;

    /**
     * 分页查询博客
     * @param pageInfo
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String blogs(PageInfo<Blog> pageInfo, BlogQuery blogQ, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.pageQuery(pageInfo, blogQ));
        return LIST;
    }

    /**
     * 分页条件查询博客
     * @param pageInfo
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(PageInfo<Blog> pageInfo, BlogQuery blogQ, Model model) {
        model.addAttribute("page", blogService.pageQuery(pageInfo, blogQ));
        //返回的数据只修改blogList
        return "admin/blogs :: blogList";
    }

    /**
     * 跳转到博客新增页面
     * @return
     */
    @GetMapping("/blogs/input")
    public String toAddBlog(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    /**
     * 封装方法
     * @param model
     */
    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    /**
     * 跳转到博客修改页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String toEditBlog(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlogById(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }

    /**
     * 新增/修改博客
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/blogs")
    public String post(@RequestParam("fileName")MultipartFile file, Blog blog, RedirectAttributes attributes, HttpSession session) throws IOException {

        //存储上传图片的网络地址
        String fdfsFilePath = "";

        if(file.isEmpty()){
            System.out.println("没有选择要上传的文件...");
        } else {
            fdfsFilePath = serverUrl + commonFileUtil.uploadFile(file);
            System.out.println("Fdfs文件服务器地址为: "+fdfsFilePath);
        }


        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getTypeById(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        if (blog.getFlag() == null || "".equals(blog.getFlag())){
            blog.setFlag("原创");
        }

        if (blog.getId() == null) {
            //新增
            Long id = new Date().getTime();
            blog.setId(id);
            if(fdfsFilePath != ""){
                blog.setFirstPicture(fdfsFilePath);
            }

            blogService.saveBlog(blog);

            Blog b = blogService.getBlogById(id);
            if (b == null ) {
                attributes.addFlashAttribute("message", "新增失败");
            } else {
                attributes.addFlashAttribute("message", "新增成功");
            }

        } else {
            //修改
            //首先删除原来的图片
            String picturePath = blogService.getBlogById(blog.getId()).getFirstPicture();

            //然后设置新的图片地址
            if(fdfsFilePath != ""){
                blog.setFirstPicture(fdfsFilePath);
                commonFileUtil.deleteFile(picturePath);
            }
            blogService.updateBlogById(blog.getId(), blog);
        }

        return REDIRECT_LIST;
    }

    /**
     * 删除博客
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.deleteBlogById(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }


    /**
     * 上传文件到本地
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadPicToLocal")
    public String uploadPicToFDFS(@RequestParam("fileName") MultipartFile file) throws Exception {

        System.out.println("上传首图地址到本地...");

        String targetFilePath = "C:/ivan/";

        if(file.isEmpty()){
            System.out.println("文件为空...");
        }

        //获取原来文件名称
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        if(!fileSuffix.equals(".jpg") && !fileSuffix.equals(".png")){
            System.out.println("文件格式不正确...");
        }

        //利用当前日期作为新文件名
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        //拼装新的文件名
        String targetFileName = targetFilePath + newFileName + fileSuffix;

        //上传文件
        try {
            FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(targetFileName));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException...");
            throw e;
        } catch (IOException e) {
            System.out.println("IOException...");
            throw e;
        }

        return "admin/success";
    }

}
