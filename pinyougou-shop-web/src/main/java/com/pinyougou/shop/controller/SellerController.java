package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * SellerController
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference(timeout = 10000)
	private SellerService sellerService;

	@PostMapping("/save")
	public boolean saveSeller(@RequestBody Seller seller) {
		try {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String pwd = encoder.encode(seller.getPassword());
			seller.setPassword(pwd);
			sellerService.saveSeller(seller);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}