package com.taotao.search.listener;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

/**
 * 监听商品添加事件，同步索引库
 */
public class ItemAddMessageListener  implements MessageListener {
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer;
    @Override
    public void onMessage(Message message) {
        try {
            //取得商品id
            TextMessage textMeaage=(TextMessage)message;
            String id = textMeaage.getText();
            long itemId=Long.parseLong(id);
            //根据商品id查询数据，取得商品信息
            //等待事务提交，可能添加商品时比较慢，事务还没提交
            Thread.sleep(1000);
            SearchItem searchItem=searchItemMapper.getItemById(itemId);
            //创建文档对象
            SolrInputDocument document=new SolrInputDocument();
            //向文档对象中添加域
            document.addField("id",searchItem.getId());
            document.addField("item_title",searchItem.getTitle());
            document.addField("item_price",searchItem.getPrice());
            document.addField("item_sell_point",searchItem.getSell_point());
            document.addField("item_image",searchItem.getImage());
            document.addField("item_category_name",searchItem.getCategory_name());
            document.addField("item_desc",searchItem.getItem_desc());
            //把文件对象写入索引库
            solrServer.add(document);
            //提交
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
