package com.pinyougou.sellergoods.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;

import java.util.List;
import java.util.Map;

/**
 * 规格服务层接口
 *
 * @author LEE.SIU.WAH
 * @version 1.0
 * @email lixiaohua7@163.com
 * @date 2017年12月7日 下午1:54:04
 */
public interface SpecificationService {

    PageResult findByPage(Specification specification, Integer page, Integer rows);

    void saveSpecification(Specification specification);

    List<SpecificationOption> findOne(Long id);

    void updateSpecification(Specification specification);

    void deleteSpecification(Long[] ids);

    List<Map<String,String>> findSpecificationByIdAndName();
}
