package com.pinyougou.manager.controller;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.TypeTemplate;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.TypeTemplateService;

/**
 * TypeTemplateController
 *
 * @author LEE.SIU.WAH
 * @version 1.0
 * @email lixiaohua7@163.com
 * @date 2017年12月7日 下午2:10:14
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;

    @GetMapping("/findByPage")
    public PageResult findByPage(TypeTemplate typeTemplate,
                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                 @RequestParam(name = "rows", defaultValue = "10") Integer rows) {
        if (typeTemplate.getName() != null && !typeTemplate.getName().trim().equals("")) {
            try {
                typeTemplate.setName(new String(typeTemplate.getName()
                        .getBytes("ISO8859-1"), "UTF8"));
            } catch (Exception e) {
                e.printStackTrace();
                typeTemplate.setName(null);
            }
        }
        PageResult result = typeTemplateService.findByPage(typeTemplate, page, rows);
        return result;
    }

    @PostMapping("/save")
    public boolean save(@RequestBody TypeTemplate typeTemplate) {
        try {
            typeTemplateService.saveTypeTemplate(typeTemplate);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @PostMapping("/update")
    public boolean update(@RequestBody TypeTemplate typeTemplate) {
        try {
            typeTemplateService.updateTypeTemplate(typeTemplate);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @GetMapping("/delete")
    public boolean delete(@RequestParam(name = "ids") Long[] ids) {
        try {
            typeTemplateService.deleteByIds(ids);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
