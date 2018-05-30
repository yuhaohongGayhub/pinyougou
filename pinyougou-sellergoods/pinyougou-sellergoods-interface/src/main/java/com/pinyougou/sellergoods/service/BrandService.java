package com.pinyougou.sellergoods.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    /**
     * 查询所有品牌
     */
    List<Brand> findAll();

    PageResult findByPage(Brand brand, int pageNum, int pageSize);

    void saveBrand(Brand brand);

    void updateBrand(Brand brand);

    void deleteBrand(Long[] ids);

    /** 查找品牌Id 和 名字 */
    List<Map<String, String>> findBrandByIdAndName();
}
