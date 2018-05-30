package com.pinyougou.sellergoods.service.impl;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Brand;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.SpecificationMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.sellergoods.service.SpecificationService;

import java.util.List;
import java.util.Map;

/**
 * 规格服务接口实现层
 */
@Service(interfaceName = "com.pinyougou.sellergoods.service.SpecificationService")
@Transactional(readOnly = false)
public class SpecificationServiceImpl implements SpecificationService {

    /**
     * 注入数据访问层代理对象
     */
    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    @Override
    public PageResult findByPage(Specification specification, Integer pageNum, Integer pageSize) {
        PageInfo<Specification> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                specificationMapper.findAll(specification);
            }
        });
        PageResult result = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return result;
    }

    @Override
    public void saveSpecification(Specification specification) {
        try {
            specificationMapper.insertSelective(specification);
            specificationOptionMapper.save(specification);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpecificationOption> findOne(Long id) {
        try {
            //SpecificationOption obj = new SpecificationOption();
            //obj.setSpecId(id);
            //findAllBySpecId
            //return specificationOptionMapper.selectByExample(obj);
            return specificationOptionMapper.findAllBySpecId(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSpecification(Specification specification) {
        try {
            specificationMapper.updateByPrimaryKeySelective(specification);
            specificationOptionMapper.deleteBySpecId(specification.getId());
            specificationOptionMapper.save(specification);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSpecification(Long[] ids) {
        try {
            for (Long id : ids) {
                specificationOptionMapper.deleteBySpecId(id);
                specificationMapper.deleteByPrimaryKey(id);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, String>> findSpecificationByIdAndName() {
        try {
            return specificationMapper.findSpecificationByIdAndName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}