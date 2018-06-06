package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.pojo.Content;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference
    private ContentService contentService;

    @GetMapping("/findByPage")
    public PageResult findByPage(Integer page, Integer rows) {
        return contentService.findByPage(page, rows);
    }

    @PostMapping("/save")
    public boolean saveContent(@RequestBody Content content) {
        try {
            contentService.saveContent(content);
            return true;
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    @PostMapping("/update")
    public boolean updateContent(@RequestBody Content content) {
        try {
            contentService.updateContent(content);
            return true;
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    @GetMapping("/delete")
    public boolean deleteContent(Long[] ids) {
        try {
            contentService.deleteContent(ids);
            return true;
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
