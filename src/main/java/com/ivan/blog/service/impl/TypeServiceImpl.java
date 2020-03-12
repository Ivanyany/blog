package com.ivan.blog.service.impl;

import com.ivan.blog.exception.NotFoundException;
import com.ivan.blog.bean.Type;
import com.ivan.blog.mapper.TypeMapper;
import com.ivan.blog.service.TypeService;
import com.ivan.blog.utils.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: Ivan
 * @Date: 2020/2/1 18:20
 * @Description:
 */
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeMapper typeMapper;

    @Transactional
    @Override
    public void saveType(Type type) {
        typeMapper.saveType(type);
    }

    @Transactional
    @Override
    public Type getTypeById(Long id) {
        return typeMapper.getTypeById(id);
    }

    @Transactional
    @Override
    public Type getTypeByName(String name) {
        return typeMapper.getTypeByName(name);
    }

    @Transactional
    @Override
    public List<Type> listType() {
        return typeMapper.listType();
    }

    /**
     * 查询前size个分类
     * @param size
     * @return
     */
    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return typeMapper.listTypeTop(pageable);
    }

    @Transactional
    @Override
    public void updateTypeById(Long id, Type type) {

        Type t = typeMapper.getTypeById(id);
        if (t == null){
            throw new NotFoundException("不存在该分类");
        }
        BeanUtils.copyProperties(type, t);
        typeMapper.updateTypeById(t);
    }

    @Transactional
    @Override
    public void deleteTypeById(Long id) {
        typeMapper.deleteTypeById(id);
    }

    //分页查询
    @Override
    public PageInfo<Type> pageQuery(PageInfo<Type> pageInfo) {

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
        Long total = typeMapper.selectCountType();
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
        pageInfo.setContent(typeMapper.pageQuery(pageInfo));
        return pageInfo;
    }
}
