package com.pinyougou.mapper;

import com.pinyougou.pojo.Goods;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface GoodsMapper extends Mapper<Goods>{


    List<Goods> selectBySellerAndGoodsName(Goods goods);
}