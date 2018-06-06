package com.pinyougou.content.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Content;

import java.util.List;

public interface ContentService {
    PageResult findByPage(Integer page, Integer rows);

    void saveContent(Content content);

    void updateContent(Content content);

    void deleteContent(Long[] ids);

    List<Content> findContentByCategoryById(Long id);
}
