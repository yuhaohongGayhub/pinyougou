package com.pinyougou.mapper;

import com.pinyougou.pojo.Seller;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SellerMapper extends Mapper<Seller> {

    List<Seller> findAll(Seller seller);
}