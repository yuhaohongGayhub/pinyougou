package com.pinyougou.sellergoods.service;


import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Goods;

import java.util.Map;

/**
 * 服务层接口
 */
public interface GoodsService {

    void saveGoods(Goods goods);

    PageResult findGoodsByPage(Goods goods, Integer page, Integer rows);

    Goods findGoodsById(Long id);

    /**
     * 获取商品信息
     */
    Map<String, Object> getGoods(Long goodsId);
}
