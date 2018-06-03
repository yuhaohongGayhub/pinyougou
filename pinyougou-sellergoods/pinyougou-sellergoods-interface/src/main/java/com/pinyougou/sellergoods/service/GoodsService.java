package com.pinyougou.sellergoods.service;


import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Goods;

/**
 * 服务层接口
 */
public interface GoodsService {

    void saveGoods(Goods goods);

    PageResult findGoodsByPage(Goods goods, Integer page, Integer rows);

    Goods findGoodsById(Long id);
}
