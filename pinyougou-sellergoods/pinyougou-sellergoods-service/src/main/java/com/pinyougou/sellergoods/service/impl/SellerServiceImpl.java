package com.pinyougou.sellergoods.service.impl;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.SellerMapper;
import com.pinyougou.sellergoods.service.SellerService;

import java.util.Date;
import java.util.List;


/**
 * 服务实现层
 */
@Service(interfaceName = "com.pinyougou.sellergoods.service.SellerService")
@Transactional(readOnly = false)
public class SellerServiceImpl implements SellerService {

    /**
     * 注入数据访问层代理对象
     */
    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public void saveSeller(Seller seller) {
        try {
            seller.setStatus("0");
            seller.setCreateTime(new Date());
            sellerMapper.insertSelective(seller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult findByPage(Seller seller, Integer page, Integer rows) {
        try {
            PageInfo pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    sellerMapper.findAll(seller);
                }
            });
            PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
            return pageResult;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateStatus(String sellerId, String status) {
        try {
            Seller seller = new Seller();
            seller.setSellerId(sellerId);
            seller.setStatus(status);
            sellerMapper.updateByPrimaryKeySelective(seller);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Seller findOne(String userName) {
        Seller seller = sellerMapper.selectByPrimaryKey(userName);
        return seller;
    }
}