package com.pinyougou.sellergoods.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.TypeTemplate;

import java.util.List;
import java.util.Map;

/**
 * 服务层接口
 * @author LEE.SIU.WAH
 * @email lixiaohua7@163.com
 * @date 2017年12月7日 下午1:54:30
 * @version 1.0
 */
public interface TypeTemplateService {

    PageResult findByPage(TypeTemplate typeTemplate, Integer page, Integer rows);

    void saveTypeTemplate(TypeTemplate typeTemplate);

    void updateTypeTemplate(TypeTemplate typeTemplate);

    void deleteByIds(Long[] ids);

    List<Map<String,String>> finTypeTemplateIdAndName();

    TypeTemplate findOne(Long id);

    List<Map> findSpecByTemplateId(Long id);

    void saveToRedis();
}
