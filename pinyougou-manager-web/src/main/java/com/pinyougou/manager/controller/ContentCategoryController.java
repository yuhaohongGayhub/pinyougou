package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.content.service.ContentCategoryService;
import com.pinyougou.pojo.ContentCategory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contentCategory")
public class ContentCategoryController {
    @Reference
    private ContentCategoryService contentCategoryService;

    @GetMapping("/findByPage")
    public PageResult findByPage(Integer page, Integer rows) {
        return contentCategoryService.findContentCategoryByPage(page, rows);
    }

    @PostMapping("/save")
    public boolean saveContentCategory(@RequestBody ContentCategory contentCategory) {
        try {
            contentCategoryService.saveContentCategory(contentCategory);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @PostMapping("/update")
    public boolean updateContentCategory(@RequestBody ContentCategory contentCategory) {
        try {
            contentCategoryService.updateContentCategory(contentCategory);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @GetMapping("/delete")
    public boolean deleteContentCategorysByIds(Long[] ids) {
        try {
            contentCategoryService.deleteContentCategoryById(ids);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @GetMapping("/findAll")
    public List<ContentCategory> findAll() {
        return contentCategoryService.findAll();
    }

}
