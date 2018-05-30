package com.pinyougou.manager.controller;

import com.pinyougou.pojo.ItemCat;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.ItemCatService;

import java.util.List;
import java.util.Map;

/**
 * ItemCatController
 *
 * @author LEE.SIU.WAH
 * @version 1.0
 * @email lixiaohua7@163.com
 * @date 2017年12月7日 下午2:08:39
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;
    @Reference
    private TypeTemplateService typeTemplateService;


    @GetMapping("/findItemCatByParentId")
    public List<ItemCat> findItemCatByParentId(
            @RequestParam(name = "parentId", defaultValue = "0") Long parentId) {
        try {
            List<ItemCat> list = itemCatService.findItemCatByParentId(parentId);
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @GetMapping("/finTypeTemplateIdAndName")
    public List<Map<String, String>> finTypeTemplateIdAndName() {
        List<Map<String, String>> list = typeTemplateService.finTypeTemplateIdAndName();
        return list;
    }

    @PostMapping("/save")
    public boolean saveItemCat(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.saveItemCat(itemCat);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/update")
    public boolean updateItemCat(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.updateItemCat(itemCat);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/delete")
    public boolean deleteItemCat(@RequestParam(name = "ids")Long[] ids) {
        try {
            itemCatService.deleteByIds(ids);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}