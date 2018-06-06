package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.content.service.ContentCategoryService;
import com.pinyougou.mapper.ContentCategoryMapper;
import com.pinyougou.pojo.ContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Service(interfaceName = "com.pinyougou.content.service.ContentCategoryService")
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private ContentCategoryMapper contentCategoryMapper;

    @Override
    public PageResult findContentCategoryByPage(int page, int rows) {
        try {
            PageInfo<Object> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    contentCategoryMapper.selectAll();
                }
            });
            PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
            return pageResult;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void saveContentCategory(ContentCategory contentCategory) {
        try {
            contentCategoryMapper.insertSelective(contentCategory);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateContentCategory(ContentCategory contentCategory) {
        try {
            contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteContentCategoryById(Long[] ids) {
        System.out.println(Arrays.toString(ids));
        try {
            for (Long id : ids) {
                contentCategoryMapper.deleteByPrimaryKey(id);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<ContentCategory> findAll() {
        try {
            return contentCategoryMapper.selectAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
