package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Brand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//RestController = Controller + ResponseBody
@RestController
public class BrandController {

    /* 引用服务接口代理对象 */
    @Reference(timeout = 10000)
    private BrandService brandService;

    /* 查询全部品牌 */
    @GetMapping("/brand/findAll")
    public List<Brand> findAll() {
        return brandService.findAll();
    }/* 查询全部品牌 */

    @GetMapping("/brand/findByPage")
    public PageResult findByPage(Brand brand,
                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                 @RequestParam(name = "rows", defaultValue = "10") Integer rows) {
        if (brand.getName() != null && !brand.getName().trim().equals("")) {
            try {
                brand.setName(new String(brand.getName().getBytes("ISO8859-1"), "UTF8"));
            } catch (Exception e) {
                e.printStackTrace();
                brand.setName(null);
            }
        }
        PageResult result = brandService.findByPage(brand, page, rows);
        return result;
    }

    @PostMapping("/brand/save")
    public boolean addBrand(@RequestBody Brand brand) {
        try {
            brandService.saveBrand(brand);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/brand/update")
    public boolean updateBrand(@RequestBody Brand brand) {
        System.out.println(brand);
        try {
            brandService.updateBrand(brand);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("brand/delete")
    public boolean delete(Long[] ids) {
        System.out.println(Arrays.toString(ids));
        try {
            if (ids != null && ids.length > 0) {
                brandService.deleteBrand(ids);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 查询所有的品牌
     */
    @GetMapping("brand/selectBrandList")
    public List<Map<String, String>> selectBrandList() {
        return brandService.findBrandByIdAndName();
    }


}
