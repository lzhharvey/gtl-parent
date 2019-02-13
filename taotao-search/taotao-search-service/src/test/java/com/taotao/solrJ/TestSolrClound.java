package com.taotao.solrJ;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrClound {

    @Test
    public void testSolrCloudAdDocument() throws  Exception{
        //创建一个CloudSolrServer对象，构造方法中需要指定zookeeper的地址列表
        CloudSolrServer cloudSolrServer=new CloudSolrServer("192.168.169.130:2181,192.168.169.130:2182,192.168.169.130:2183");
        //需要设置默认的Collection
        cloudSolrServer.setDefaultCollection("collection2");
        //创建一个文档对象
        SolrInputDocument document=new SolrInputDocument();
        //向文档中添加域
        document.addField("id","test001");
        document.addField("item_title","测试商品名称");
        document.addField("item_price",100);
        //把文档写入索引域
        cloudSolrServer.add(document);
        //提交
        cloudSolrServer.commit();

    }

}
