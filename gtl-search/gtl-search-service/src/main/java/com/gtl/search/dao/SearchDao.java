package com.gtl.search.dao;

import com.gtl.common.pojo.SearchItem;
import com.gtl.common.pojo.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询索引库商品dao
 */
@Repository
public class SearchDao {
    @Autowired
    private SolrServer solrServer;

    public SearchResult search(SolrQuery solrQuery) throws Exception{
        //根据query对象进行查询
        QueryResponse response= solrServer.query(solrQuery);
        //取查询结果
        SolrDocumentList solrDocumentList =response.getResults();
        //去查询结果总记录数
        long numFound =solrDocumentList.getNumFound();
        SearchResult result=new SearchResult();
        result.setRecordCount(numFound);

        List<SearchItem> itemList=new ArrayList<>();

        //把查询结果封装到SearchItem对象中
        for(SolrDocument solrDocument: solrDocumentList) {
            SearchItem searchItem = new SearchItem();
            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemtitle = "";
            if (list != null && list.size() > 0) {
                itemtitle = list.get(0);
            } else {
                itemtitle = (String) solrDocument.get("item_title");
            }
            searchItem.setTitle(itemtitle);
            searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
            searchItem.setPrice((Long) solrDocument.get("item_price"));
            //取一张图片
            String image=(String)solrDocument.get("item_image");
            if(StringUtils.isNotBlank(image)){
                image=image.split(",")[0];
            }
            searchItem.setImage(image);
            searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
            searchItem.setItem_desc((String) solrDocument.get("item_desc"));
            searchItem.setId((String)solrDocument.get("id"));
            itemList.add(searchItem);
        }
        result.setItemList(itemList);
        return result;
    }

}
