package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * GoodsController
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    @PostMapping("/save")
    public boolean saveGoods(@RequestBody Goods goods) {
        try {
            //获取商家id
            System.out.println(goods);
            SecurityContext context = SecurityContextHolder.getContext();
            String sellerId = context.getAuthentication().getName();
            goods.setSellerId(sellerId);
            goodsService.saveGoods(goods);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
