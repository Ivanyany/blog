package com.ivan.blog.utils;

import java.util.List;

/**
 * 分页信息
 * @Auther: Ivan
 * @Date: 2020/2/4 10:17
 * @Description:
 */
public class PageInfo<T> {

    //当前是第几页
    private Long pageNum;

    //每页的数据量
    private Long pageSize;

    //从第几个数据开始查
    private Long startNum;

    //总记录数
    private Long total;

    //总页数
    private Long pages;

    //是否是第一页
    private Boolean first;

    //是否是最后一页
    private Boolean last;

    //结果集
    private List<T> content;

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getStartNum() {
        return startNum;
    }

    public void setStartNum(Long startNum) {
        this.startNum = startNum;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
