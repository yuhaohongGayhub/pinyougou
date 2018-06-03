package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
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

    @GetMapping("/search")
    public PageResult search(Goods goods,
                             @RequestParam(name = "page") Integer page,
                             @RequestParam(name = "rows") Integer rows) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.setSellerId(name);
            if (goods.getGoodsName() != null && goods.getGoodsName().trim().length() != 0) {
                byte[] bytes = goods.getGoodsName().getBytes("ISO-8859-1");
                String goodsName = new String(bytes, "utf-8");
                goods.setGoodsName(goodsName);
            }else {
                goods.setGoodsName(null);
            }
            PageResult result = goodsService.findGoodsByPage(goods, page, rows);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @GetMapping("/findOne")
    public Goods findOne(@RequestParam(name = "id")Long id) {
        return goodsService.findGoodsById(id);
    }
}
