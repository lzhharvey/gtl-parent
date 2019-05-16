package com.gtl.search.listener;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.management.Query;

public class ItemDeleteMessageListener implements MessageListener {
    @Autowired
    private SolrServer solrServer;
    @Override
    public void onMessage(Message message) {
        try {
        //获取商品id
        TextMessage textMeaage=(TextMessage)message;
        String id = textMeaage.getText();

        //删除索引库中对应商品
        solrServer.deleteById(id);

        solrServer.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //删除索引库中的商品

    }
}
