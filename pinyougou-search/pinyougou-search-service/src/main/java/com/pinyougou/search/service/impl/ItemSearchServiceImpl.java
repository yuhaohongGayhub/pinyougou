package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.Brand;
import com.pinyougou.pojo.SolrItem;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.security.Key;
import java.util.*;

@Service(interfaceName = "com.pinyougou.search.service.ItemSearchService")
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> params) {
        Map<String, Object> data = new HashMap<>();
        //分页选项
        Integer curPage = (Integer) params.get("curPage");
        if (curPage == null || curPage < 1) {
            curPage = 1;
        }
        Integer pageSize = (Integer) params.get("pageSize");
        if (pageSize == null || pageSize <= 0) {
            pageSize = 20;
        }

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

            //分类过滤
            String category = (String) params.get("category");
            if (category != null && !category.trim().equals("")) {
                Criteria categoryCriteria = new Criteria("category").is(category);
                SimpleFilterQuery categoryFilter = new SimpleFilterQuery();
                categoryFilter.addCriteria(categoryCriteria);
                highlightQuery.addFilterQuery(categoryFilter);
            }

            //品牌过滤
            String brand = (String) params.get("brand");
            if (brand != null && !brand.trim().equals("")) {
                Criteria brandCriteria = new Criteria("brand").is(params.get("brand"));
                SimpleFilterQuery brandFilter = new SimpleFilterQuery(brandCriteria);
                highlightQuery.addFilterQuery(brandFilter);
            }

            //规格选项过滤
            Map<String, String> specs = (Map<String, String>) params.get("spec");
            if (specs != null && specs.size() > 0) {
                Set<Map.Entry<String, String>> entries = specs.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    Criteria specCriteria = new Criteria("spec_" + entry.getKey()).is(entry.getValue());
                    highlightQuery.addFilterQuery(new SimpleFilterQuery(specCriteria));
                }
            }

            //价格区间过滤
            String priceStr = (String) params.get("price");
            if (priceStr != null && !priceStr.trim().equals("")) {
                String[] prices = priceStr.split("-");
                if (!"0".equals(prices[0])) {
                    Criteria firstPriceCriteria = new Criteria("price").greaterThanEqual(prices[0]);
                    SimpleFilterQuery priceFilter = new SimpleFilterQuery();
                    priceFilter.addCriteria(firstPriceCriteria);
                    highlightQuery.addCriteria(firstPriceCriteria);
                }
                if (!"*".equals(prices[1])) {
                    Criteria lastPriceCriteria = new Criteria("price").lessThanEqual(prices[1]);
                    SimpleFilterQuery priceFilter = new SimpleFilterQuery();
                    priceFilter.addCriteria(lastPriceCriteria);
                    highlightQuery.addCriteria(lastPriceCriteria);
                }
            }
            //分页
            highlightQuery.setOffset((curPage - 1) * pageSize);
            highlightQuery.setRows(pageSize);

            String sortField = (String) params.get("sortField");
            String sortDir = (String) params.get("sortDir");
            if (sortField != null && !sortField.trim().equals("")) {
                Sort sort = new Sort(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
                highlightQuery.addSort(sort);
            }

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

            if (category != null && !category.trim().equals("")) {
                Map<String, Object> ret = searchBrandAndSpecList(category);
                data.putAll(ret);
            } else {
                //从缓存中取值
                if (categoryList != null && categoryList.size() > 0) {
                    String categoryName = categoryList.get(0);
                    Map<String, Object> ret = searchBrandAndSpecList(categoryName);
                    data.putAll(ret);
                }
            }

            //分页值
            long totalElements = solrItems.getTotalElements();
            int totalPages = solrItems.getTotalPages();
            data.put("totalPages", totalPages);
            data.put("total", totalElements);
            return data;
        } else {
            SimpleQuery query = new SimpleQuery("*:*");
            query.setOffset((curPage - 1) * pageSize);
            query.setRows(pageSize);
            ScoredPage<SolrItem> solrItems = solrTemplate.queryForPage(query, SolrItem.class);
            List<SolrItem> content = solrItems.getContent();
            System.out.println(content);
            data.put("rows", content);

            int totalPages = solrItems.getTotalPages();
            long totalElements = solrItems.getTotalElements();
            data.put("totalPages", totalPages);
            data.put("total", totalElements);
            return data;
        }
    }

    //根据分类名称查询品牌和规格选项数据(Redis)
    private Map<String, Object> searchBrandAndSpecList(String category) {
        Map<String, Object> data = new HashMap<>();
        //通过分类名称获取类型模板Id
        Long typeId = (Long) redisTemplate.boundHashOps("itemCast").get(category);
        if (typeId != null && typeId > 0) {
            List<Brand> brandList = (List<Brand>) redisTemplate.boundHashOps("brandList").get(typeId);
            data.put("brandList", brandList);
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            data.put("specList", specList);
        }
        return data;
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
        SolrTemplate solrTemplate = container.getBean(SolrTemplate.class);

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
        Criteria criteria = new Criteria("keywords").is("小米");
        highlightQuery.addCriteria(criteria);
        HighlightPage<SolrItem> highlightPage = solrTemplate.queryForHighlightPage(highlightQuery, SolrItem.class);
        List<SolrItem> items = highlightPage.getContent();
        for (SolrItem item : items) {
            System.out.println(item);
        }
    }

}
