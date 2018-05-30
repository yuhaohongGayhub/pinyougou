package com.pinyougou.sellergoods.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.pojo.TypeTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TypeTemplateMapper;
import com.pinyougou.sellergoods.service.TypeTemplateService;

import java.util.List;
import java.util.Map;


/**
 * 服务实现层
 *
 * @author LEE.SIU.WAH
 * @version 1.0
 * @email lixiaohua7@163.com
 * @date 2017年12月7日 下午2:00:03
 */
@Service(interfaceName = "com.pinyougou.sellergoods.service.TypeTemplateService")
@Transactional(readOnly = false)
public class TypeTemplateServiceImpl implements TypeTemplateService {

    /**
     * 注入数据访问层代理对象
     */
    @Autowired
    private TypeTemplateMapper typeTemplateMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    @Override
    public PageResult findByPage(TypeTemplate typeTemplate, Integer pageNum, Integer pageSize) {
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                typeTemplateMapper.findAll(typeTemplate);
            }
        });
        PageResult result = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return result;
    }

    @Override
    public void saveTypeTemplate(TypeTemplate typeTemplate) {
        try {
            typeTemplateMapper.insertSelective(typeTemplate);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateTypeTemplate(TypeTemplate typeTemplate) {
        try {
            typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteByIds(Long[] ids) {
        try {
            for (Long id : ids) {
                typeTemplateMapper.deleteByPrimaryKey(id);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Map<String, String>> finTypeTemplateIdAndName() {
        try {
            List<Map<String, String>> list = typeTemplateMapper.finTypeTemplateIdAndName();
            return list;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public TypeTemplate findOne(Long id) {
        try {
            return typeTemplateMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map> findSpecByTemplateId(Long id) {
        TypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
        //[{"id":26,"text":"尺码"},{"id":35,"text":"安全套"},{"id":34,"text":"震动棒"}]
        String specIds = typeTemplate.getSpecIds();
        List<Map> maps = JSON.parseArray(specIds, Map.class);
        for (Map map : maps) {
            Long specId = Long.valueOf(map.get("id").toString());
            SpecificationOption option = new SpecificationOption();
            option.setSpecId(specId);
            List<SpecificationOption> options = specificationOptionMapper.select(option);
            map.put("options", options);
        }
        return maps;
    }
}