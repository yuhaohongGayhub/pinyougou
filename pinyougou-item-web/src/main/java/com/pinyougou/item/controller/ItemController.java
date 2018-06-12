package com.pinyougou.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class ItemController {
    @Reference
    private GoodsService goodsService;

    @GetMapping("/{goodsId}")
    public String getGoods(@PathVariable(name = "goodsId") Long goodsId, Model model) {
        Map<String, Object> data = goodsService.getGoods(goodsId);
        model.addAllAttributes(data);
        return "item";
    }
}
