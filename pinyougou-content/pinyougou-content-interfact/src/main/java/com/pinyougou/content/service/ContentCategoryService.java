package com.pinyougou.content.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.ContentCategory;

import java.util.List;

public interface ContentCategoryService {
    PageResult findContentCategoryByPage(int page, int row);

    void saveContentCategory(ContentCategory contentCategory);

    void updateContentCategory(ContentCategory contentCategory);

    void deleteContentCategoryById(Long[] ids);

    List<ContentCategory> findAll();
}
