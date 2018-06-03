package com.pinyougou.sellergoods.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.GoodsDesc;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.sellergoods.service.GoodsService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 */
@Service(interfaceName = "com.pinyougou.sellergoods.service.GoodsService")
@Transactional(readOnly = false)
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public void saveGoods(Goods goods) {
        try {
            /** 设置未申核状态 */
            goods.setAuditStatus("0");
            goodsMapper.insert(goods);

            goods.getGoodsDesc().setGoodsId(goods.getId());
            goodsDescMapper.insert(goods.getGoodsDesc());

            List<Item> items = goods.getItems();
            for (Item item : items) {
                /** 定义具体商品的标题 */
                StringBuilder title = new StringBuilder();
                title.append(goods.getGoodsName());
                /** 把规格选项JSON字符串转化成Map集合 */
                Map<String, Object> spec = JSON.parseObject(item.getSpec());
                for (Object value : spec.values()) {
                    /** 拼接规格选项到具体商品标题 */
                    title.append(" ").append(value);
                }
                /** 设置SKU商品的标题 */
                item.setTitle(title.toString());
                /** 设置SKU商品图片地址 */
                List<Map> imageList = JSON.parseArray(
                        goods.getGoodsDesc().getItemImages(), Map.class);
                if (imageList != null && imageList.size() > 0) {
                    /** 取第一张图片 */
                    item.setImage((String) imageList.get(0).get("url"));
                }
                /** 设置SKU商品的分类(三级分类) */
                item.setCategoryid(goods.getCategory3Id());
                /** 设置SKU商品的创建时间 */
                item.setCreateTime(new Date());
                /** 设置SKU商品的修改时间 */
                item.setUpdateTime(item.getCreateTime());
                /** 设置SPU商品的编号 */
                item.setGoodsId(goods.getId());
                /** 设置商家编号 */
                item.setSellerId(goods.getSellerId());
                /** 设置分类名称 */
                item.setCategory(itemCatMapper
                        .selectByPrimaryKey(goods.getCategory3Id()).getName());
                /** 设置品牌名称 */
                item.setBrand(brandMapper
                        .selectByPrimaryKey(goods.getBrandId()).getName());
                /** 设置商家店铺名称 */
                item.setSeller(sellerMapper.selectByPrimaryKey(
                        goods.getSellerId()).getNickName());
                itemMapper.insertSelective(item);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PageResult findGoodsByPage(Goods goods, Integer page, Integer rows) {
        PageInfo<Map> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                goodsMapper.selectBySellerAndGoodsName(goods);
            }
        });
        List<Map> list = pageInfo.getList();
        for (Map map : list) {
            ItemCat category1Id = itemCatMapper.selectByPrimaryKey(map.get("category1Id"));
            ItemCat category2Id = itemCatMapper.selectByPrimaryKey(map.get("category2Id"));
            ItemCat category3Id = itemCatMapper.selectByPrimaryKey(map.get("category3Id"));
            map.put("category1Name", category1Id.getName());
            map.put("category2Name", category2Id.getName());
            map.put("category3Name", category3Id.getName());
        }
        PageResult result = new PageResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }

    @Override
    public Goods findGoodsById(Long id) {
        try {
            //SPU
            Goods goods = goodsMapper.selectByPrimaryKey(id);
            GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);
            goods.setGoodsDesc(goodsDesc);
            //SKU
            Item item = new Item();
            item.setGoodsId(goods.getId());
            List<Item> items = itemMapper.select(item);
            goods.setItems(items);
            return goods;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}