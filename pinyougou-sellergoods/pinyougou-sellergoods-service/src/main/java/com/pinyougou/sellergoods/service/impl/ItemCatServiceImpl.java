package com.pinyougou.sellergoods.service.impl;

import com.pinyougou.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.ItemCatMapper;
import com.pinyougou.sellergoods.service.ItemCatService;

import java.util.List;

/**
 * 服务实现层
 *
 * @author LEE.SIU.WAH
 * @version 1.0
 * @email lixiaohua7@163.com
 * @date 2017年12月7日 下午1:56:56
 */
@Service(interfaceName = "com.pinyougou.sellergoods.service.ItemCatService")
@Transactional(readOnly = false)
public class ItemCatServiceImpl implements ItemCatService {

    /**
     * 注入数据访问层代理对象
     */
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ItemCat> findItemCatByParentId(Long parentId) {
        try {
            ItemCat itemCat = new ItemCat();
            itemCat.setParentId(parentId);
            List<ItemCat> itemCats = itemCatMapper.select(itemCat);
            return itemCats;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void saveItemCat(ItemCat itemCat) {
        try {
            itemCatMapper.insertSelective(itemCat);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateItemCat(ItemCat itemCat) {
        try {
            itemCatMapper.updateByPrimaryKeySelective(itemCat);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteByIds(Long[] ids) {
        try {
            for (Long id : ids) {
                itemCatMapper.deleteByPrimaryKey(id);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void saveToRedis() {
        try {
            List<ItemCat> itemCats = itemCatMapper.selectAll();
            for (ItemCat itemCat : itemCats) {
                redisTemplate.boundHashOps("itemCast").delete(itemCat.getName(), itemCat.getTypeId());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
