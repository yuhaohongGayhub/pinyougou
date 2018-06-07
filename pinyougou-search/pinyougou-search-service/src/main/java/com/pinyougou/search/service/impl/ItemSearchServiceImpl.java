package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.SolrItem;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
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
        //获取关键字
        String keyword = (String) params.get("keywords");
        // 如果关键字不为空，则高亮查询
        if (keyword != null && keyword.trim().length() != 0) {
            //创建高亮查询对象
            HighlightQuery highlightQuery = new SimpleHighlightQuery();
            //############## 创建高亮选项对象 ############################
            HighlightOptions highlightOptions = new HighlightOptions();
            //设置需要高亮的字段
            highlightOptions.addField("title");
            //设置前缀
            highlightOptions.setSimplePrefix("<font color='red'>");
            //设置后缀
            highlightOptions.setSimplePostfix("</font>");
            //设置高亮先选项对象
            highlightQuery.setHighlightOptions(highlightOptions);
            //#########################################################
            //按关键字进行搜索
            Criteria criteria = new Criteria("keywords").is(keyword);
            highlightQuery.addCriteria(criteria);
            //########### 执行查询 #############################
            HighlightPage<SolrItem> solrItems = solrTemplate.queryForHighlightPage(highlightQuery, SolrItem.class);
            //获取到高亮的对象集合
            List<HighlightEntry<SolrItem>> highlightEntries = solrItems.getHighlighted();
            //遍历得到每一个高亮对象
            for (HighlightEntry<SolrItem> highlightEntry : highlightEntries) {
                //得到solrItem
                SolrItem solrItem = highlightEntry.getEntity();
                //得到所有的高亮字段列表
                List<HighlightEntry.Highlight> hightLights = highlightEntry.getHighlights();
                //如果title有高亮
                if (hightLights != null && hightLights.size() > 0) {
                    //得到高亮的field
                    HighlightEntry.Highlight highlight = hightLights.get(0);
                    List<String> snipplets = highlight.getSnipplets();
                    String s = snipplets.get(0);
                    solrItem.setTitle(s);
                }
            }
            List<SolrItem> content = solrItems.getContent();
            data.put("rows", content);
            //查询分类
            List<String> categoryList = searchCategoryByKeyWords(keyword);
            data.put("categoryList", categoryList);
            return data;
        } else {
            SimpleQuery query = new SimpleQuery("*:*");
            ScoredPage<SolrItem> solrItems = solrTemplate.queryForPage(query, SolrItem.class);
            List<SolrItem> content = solrItems.getContent();
            System.out.println(content);
            data.put("rows", content);
            return data;
        }
    }

    //查询所有的分类
    private List<String> searchCategoryByKeyWords(String keyWords) {
        List<String> categoryList = new ArrayList<>();
        //创建查询对象
        SimpleQuery query = new SimpleQuery("*:*");
        //创建查询对象
        Criteria criteria = new Criteria("title").is(keyWords);
        query.addCriteria(criteria);
        //添加分组查询
        GroupOptions groupOptions = new GroupOptions().addGroupByField("category");
        query.setGroupOptions(groupOptions);
        //执行查询
        GroupPage<SolrItem> groupPage = solrTemplate.queryForGroupPage(query, SolrItem.class);
        GroupResult<SolrItem> groupResult = groupPage.getGroupResult("category");
        Page<GroupEntry<SolrItem>> groupEntries = groupResult.getGroupEntries();
        List<GroupEntry<SolrItem>> content = groupEntries.getContent();
        for (GroupEntry<SolrItem> entry : content) {
            categoryList.add(entry.getGroupValue());
        }
        return categoryList;
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
