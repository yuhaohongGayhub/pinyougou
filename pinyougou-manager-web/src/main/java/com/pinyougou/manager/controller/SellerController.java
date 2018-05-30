package com.pinyougou.manager.controller;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Seller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.SellerService;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    @GetMapping("/findByPage")
    public PageResult findByPage(Seller seller,
                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                 @RequestParam(name = "rows", defaultValue = "10") Integer rows) {
        try {
            if (seller.getName() != null && !seller.getName().trim().equals("")) {
                seller.setName(new String(seller.getName().getBytes("ISO8859-1"), "utf-8"));
            }
            if (seller.getNickName() != null && !seller.getNickName().trim().equals("")) {
                seller.setNickName(new String(seller.getNickName().getBytes("ISO8859-1"), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            seller.setNickName(null);
            seller.setName(null);
        }
        PageResult result = sellerService.findByPage(seller, page, rows);
        return result;
    }

    @GetMapping("/updateStatus")
    public boolean updateStatus(@RequestParam(name = "sellerId") String sellerId,
                                @RequestParam(name = "status") String status) {
        try {
            sellerService.updateStatus(sellerId, status);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}