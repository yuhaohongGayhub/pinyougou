package com.pinyougou.manager.controller;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Brand;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.SpecificationService;

import java.util.List;
import java.util.Map;

/**
 * 规格处理器
 */
@RestController
@RequestMapping("/specification")
public class SpecificationController {
    @Reference
    private SpecificationService specificationService;

    @GetMapping("findByPage")
    public PageResult findByPage(Specification specification,
                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                 @RequestParam(name = "rows", defaultValue = "10") Integer rows) {
        if (specification.getSpecName() != null && !specification.getSpecName().trim().equals("")) {
            try {
                specification.setSpecName(new String(specification.getSpecName().getBytes("ISO8859-1"), "UTF8"));
            } catch (Exception e) {
                e.printStackTrace();
                specification.setSpecName(null);
            }
        }
        PageResult result = specificationService.findByPage(specification, page, rows);
        return result;
    }

    @PostMapping("/save")
    public boolean addBrand(@RequestBody Specification specification) {
        try {
            specificationService.saveSpecification(specification);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/findOne")
    public List<SpecificationOption> findById(@RequestParam(name = "id") Long id) {
        return specificationService.findOne(id);
    }

    @PostMapping("/update")
    public boolean update(@RequestBody Specification specification) {
        try {
            specificationService.updateSpecification(specification);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/delete")
    public boolean delete(@RequestParam(name = "ids") Long[] ids) {
        try {
            specificationService.deleteSpecification(ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 查询所有的规格 */
    @GetMapping("/selectSpecList")
    public List<Map<String, String>> selectSpecList(){
        return specificationService.findSpecificationByIdAndName();
    }
}
