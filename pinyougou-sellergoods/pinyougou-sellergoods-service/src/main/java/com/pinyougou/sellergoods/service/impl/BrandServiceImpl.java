package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.pojo.Brand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

//上面指定接口名，产生服务名，不然会用代理类的名称
//主键注册，顺便发布dubbo服务
@Service(interfaceName = "com.pinyougou.sellergoods.service.BrandService", timeout = 6000)
@Transactional(readOnly = false)
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> findAll() {
        List<Brand> list = brandMapper.selectAll();
        return list;
    }

    @Override
    public PageResult findByPage(Brand brand, int pageNum, int pageSize) {
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                brandMapper.findAll(brand);
            }
        });
        PageResult result = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return result;
    }

    @Override
    public void saveBrand(Brand brand) {
        try {
            int rows = brandMapper.insertSelective(brand);
            System.out.println("add brand = " + rows);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateBrand(Brand brand) {
        try {
            int rows = brandMapper.updateByPrimaryKeySelective(brand);
            System.out.println("update brand = " + rows);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteBrand(Long[] ids) {
        try {
            brandMapper.deleteAll(ids);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, String>> findBrandByIdAndName() {
        try {
            return brandMapper.findAllByIdAndName();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
