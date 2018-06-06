package com.pinyougou.search.service;

import java.util.Map;

public interface ItemSearchService {
    /**
     * 搜索方法
     */
    Map<String, Object> search(Map<String, Object> params);
}
