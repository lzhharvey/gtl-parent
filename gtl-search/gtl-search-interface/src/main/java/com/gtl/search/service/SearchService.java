package com.gtl.search.service;

import com.gtl.common.pojo.SearchResult;

public interface SearchService {
    SearchResult search(String queryString,int page,int rows) throws Exception;
}
