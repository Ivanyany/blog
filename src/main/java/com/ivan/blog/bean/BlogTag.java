package com.ivan.blog.bean;

/**
 * @Auther: Ivan
 * @Date: 2020/2/12 18:17
 * @Description:
 */
public class BlogTag {

    private Long blogId;
    private Long tagId;

    public BlogTag() {
    }

    public BlogTag(Long blogId, Long tagId) {
        this.blogId = blogId;
        this.tagId = tagId;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
