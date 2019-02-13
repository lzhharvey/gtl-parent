package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 搜索服务功能实现
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(String queryString, int page, int rows) throws  Exception {
        //根据查询条件拼装查询对象
        //创建一个SolrQuery对象
        SolrQuery solrQuery=new SolrQuery();
        //设置查询条件
        solrQuery.setQuery(queryString);
        //设置分页条件
        if(page<1) page=1;
        solrQuery.setStart((page-1)*rows);
        if(rows<1) rows=1;
        solrQuery.setRows(rows);
        //设置默认搜索域
        solrQuery.set("df","item_title");
        //设置高亮显示
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        //调用dao执行查询
        SearchResult searchResult=searchDao.search(solrQuery);
        //计算查询结果的总页数
        long recordCount=searchResult.getRecordCount();
        long pages=recordCount/rows;
        if(recordCount % rows>0){
            pages++;
        }
        searchResult.setTotalPages(pages);
        //返回结果
        return searchResult;
    }
}
