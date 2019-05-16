package com.gtl.search.service.impl;

import com.gtl.common.pojo.SearchItem;
import com.gtl.common.pojo.gtlResult;
import com.gtl.search.mapper.SearchItemMapper;
import com.gtl.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 商品数据导入索引库
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public gtlResult importItemToIndex() {
        try{
        //1.查询所有商品数据
        List<SearchItem> list= searchItemMapper.getItemList();
        //2.遍历商品数据添加到索引库
        for (SearchItem searchItem: list) {
            //2.1创建文档对象
            SolrInputDocument document=new SolrInputDocument();
            //2.2向文档中添加域
            document.addField("id",searchItem.getId());
            document.addField("item_title",searchItem.getTitle());
            document.addField("item_price",searchItem.getPrice());
            document.addField("item_sell_point",searchItem.getSell_point());
            document.addField("item_image",searchItem.getImage());
            document.addField("item_category_name",searchItem.getCategory_name());
            document.addField("item_desc",searchItem.getItem_desc());
            //2.3把文档写入索引库
            solrServer.add(document);
        }
        //3.提交
            solrServer.commit();
        }
        catch (Exception exception){
            exception.printStackTrace();
            return gtlResult.build(500,"数据导入失败");
        }
        //4.返回添加成功
        return gtlResult.ok();
    }
}
