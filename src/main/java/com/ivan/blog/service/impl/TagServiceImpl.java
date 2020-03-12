package com.ivan.blog.service.impl;

import com.ivan.blog.exception.NotFoundException;
import com.ivan.blog.bean.Tag;
import com.ivan.blog.mapper.TagMapper;
import com.ivan.blog.service.TagService;
import com.ivan.blog.utils.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Ivan
 * @Date: 2020/2/11 14:20
 * @Description:
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagMapper tagMapper;

    @Override
    public void saveTag(Tag tag) {
        tagMapper.saveTag(tag);
    }

    @Override
    public Tag getTagById(Long id) {
        return tagMapper.getTagById(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagMapper.getTagByName(name);
    }

    @Override
    public List<Tag> listTag() {
        return tagMapper.listTag();
    }

    @Override
    public List<Tag> listTag(String ids) {

        List<Tag> tags = new ArrayList<>();
        for(Long id : convertToList(ids)){
            tags.add(tagMapper.getTagById(id));
        }
        return tags;
    }

    //Long字符串转数组
    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    @Override
    public void updateTagById(Long id, Tag tag) {
        Tag t = tagMapper.getTagById(id);
        if (t == null){
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag, t);
        tagMapper.updateTagById(t);
    }

    @Override
    public void deleteTagById(Long id) {
        tagMapper.deleteTagById(id);
    }

    @Override
    public PageInfo<Tag> pageQuery(PageInfo<Tag> pageInfo) {
        //当前是第几页,默认查询第1页
        Long pageNum =pageInfo.getPageNum() == null ? 1 : pageInfo.getPageNum();
        pageInfo.setPageNum(pageNum);

        //每页的数据量,默认每页5行数据
        Long pageSize = pageInfo.getPageSize() == null ? 5 : pageInfo.getPageSize();
        pageInfo.setPageSize(pageSize);

        //从第几个数据开始查
        Long startNum = (pageInfo.getPageNum()-1)*pageInfo.getPageSize();
        pageInfo.setStartNum(startNum);

        //总记录数
        Long total = tagMapper.selectCountTag();
        pageInfo.setTotal(total);

        //总页数
        Long pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        pageInfo.setPages(pages);

        //是否是第一页
        if(pageNum == 1){
            pageInfo.setFirst(true);
        }

        //是否是最后一页
        if(pageNum == pages){
            pageInfo.setLast(true);
        }

        //结果集
        pageInfo.setContent(tagMapper.pageQuery(pageInfo));
        return pageInfo;
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return tagMapper.listTagTop(pageable);
    }
}
