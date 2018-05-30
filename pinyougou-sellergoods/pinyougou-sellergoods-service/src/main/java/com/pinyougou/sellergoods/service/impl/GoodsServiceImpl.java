package com.pinyougou.sellergoods.service.impl;

import com.pinyougou.mapper.GoodsDescMapper;
import com.pinyougou.pojo.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.GoodsMapper;
import com.pinyougou.sellergoods.service.GoodsService;

/**
 * 服务实现层
 */
@Service(interfaceName = "com.pinyougou.sellergoods.service.GoodsService")
@Transactional(readOnly = false)
public class GoodsServiceImpl implements GoodsService {

    /**
     * 注入数据访问层代理对象
     */
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;

    @Override
    public void saveGoods(Goods goods) {
        try {
            /** 设置未申核状态 */
            goods.setAuditStatus("0");
            goodsMapper.insert(goods);

            goods.getGoodsDesc().setGoodsId(goods.getId());
            goodsDescMapper.insert(goods.getGoodsDesc());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}