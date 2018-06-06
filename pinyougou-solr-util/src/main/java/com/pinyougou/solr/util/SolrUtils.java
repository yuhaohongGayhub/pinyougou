package com.pinyougou.solr.util;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.SolrItem;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SolrUtils {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 导入商品
     */
    public void importItemData() {
        Item searchItem = new Item();
        searchItem.setStatus("1");
        List<SolrItem> solrItems = new ArrayList<>();
        List<Item> items = itemMapper.select(searchItem);
        for (Item item : items) {
            SolrItem solrItem = new SolrItem();
            solrItem.setId(item.getId());
            solrItem.setBrand(item.getBrand());
            solrItem.setCategory(item.getCategory());
            solrItem.setGoodsId(item.getGoodsId());
            solrItem.setImage(item.getImage());
            solrItem.setPrice(item.getPrice());
            solrItem.setSeller(item.getSeller());
            solrItem.setTitle(item.getTitle());
            solrItem.setUpdateTime(item.getUpdateTime());
            String spec = item.getSpec();
            Map map = JSON.parseObject(spec, Map.class);
            solrItem.setSpecMap(map);
            solrItems.add(solrItem);
        }
        System.out.println(solrItems.size());
        UpdateResponse response = solrTemplate.saveBeans(solrItems);
        if (response.getStatus() == 0) {
            System.out.println("导入成功");
            solrTemplate.commit();
        } else {
            solrTemplate.rollback();
            System.out.println("导入失败");
        }
    }

    public static void main(String[] args) {
        ApplicationContext container =
                new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        SolrUtils solrUtils = container.getBean(SolrUtils.class);
        solrUtils.importItemData();
    }
}
