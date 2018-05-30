package com.pinyougou.sellergoods.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Seller; /**
 * 服务层接口
 * @author LEE.SIU.WAH
 * @email lixiaohua7@163.com
 * @date 2017年12月7日 下午1:53:42
 * @version 1.0
 */
public interface SellerService {

    void saveSeller(Seller seller);

    PageResult findByPage(Seller seller, Integer page, Integer rows);

    void updateStatus(String sellerId, String status);

    Seller findOne(String userName);
}
