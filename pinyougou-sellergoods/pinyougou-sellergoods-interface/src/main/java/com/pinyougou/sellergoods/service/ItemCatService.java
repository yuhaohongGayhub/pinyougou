package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.ItemCat;

import java.util.List;

/**
 * 服务层接口
 */
public interface ItemCatService {

    List<ItemCat> findItemCatByParentId(Long parentId);

    void saveItemCat(ItemCat itemCat);

    void updateItemCat(ItemCat itemCat);

    void deleteByIds(Long[] ids);
}
