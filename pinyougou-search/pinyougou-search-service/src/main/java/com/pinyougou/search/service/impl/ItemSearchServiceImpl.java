package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.SolrItem;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "com.pinyougou.search.service.ItemSearchService")
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> params) {
        Map<String, Object> data = new HashMap<>();
        Query query = new SimpleQuery("*:*");
        //获取关键字
        String keyword = (String) params.get("keywords");
        if (keyword != null && keyword.trim().length() != 0) {
            Criteria criteria = new Criteria("keywords").is(keyword);
            query.addCriteria(criteria);
        }
        ScoredPage<SolrItem> solrItems = solrTemplate.queryForPage(query, SolrItem.class);
        List<SolrItem> content = solrItems.getContent();
        System.out.println(content);
        data.put("rows",content);
        return data;
    }

    public static void main(String[] args) {
        ApplicationContext container = new ClassPathXmlApplicationContext("classpath:applicationContext-solr.xml");
        SolrTemplate template = container.getBean(SolrTemplate.class);
        Query query = new SimpleQuery("*:*");
        ScoredPage<SolrItem> solrItems = template.queryForPage(query, SolrItem.class);
        List<SolrItem> items = solrItems.getContent();
        for (SolrItem item : items) {
            System.out.println(item);
        }
    }

}
