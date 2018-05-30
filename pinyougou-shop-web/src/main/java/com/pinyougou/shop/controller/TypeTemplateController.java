package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {
    @Reference
    private TypeTemplateService typeTemplateService;

    @GetMapping("/findOne")
    public TypeTemplate findOne(@RequestParam(name = "id") Long id) {
        return typeTemplateService.findOne(id);
    }

    @GetMapping("findSpecByTemplateId")
    public List<Map> findSpecByTemplateId(@RequestParam(name = "id") Long id) {
        return typeTemplateService.findSpecByTemplateId(id);
    }
}
