package com.gtl.solrJ;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestSolrJ {

    @Test
    public void testAddDocument() throws  Exception{
        //创建一个SolrServer对象，创建一个HttpSolrServer对象
        //需要指定Solr服务的url
        SolrServer solrServer=new HttpSolrServer("http://192.168.169.130:8080/solr/collection1");
        //创建一个文档对象SolrInputDocument
        SolrInputDocument document=new SolrInputDocument();
        //向文档中添加域，必须有id域，域的名称必须在schema.xml中
        document.addField("id","test002");
        document.addField("item_title","测试商品2");
        document.addField("item_price",40000);
        //把文档对象写入索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }

    //根据id删除
    @Test
    public void deleteDocumentById() throws  Exception{
        SolrServer solrServer=new HttpSolrServer("http://192.168.169.130:8080/solr/collection1");
        solrServer.deleteById("test001");
        solrServer.commit();
    }

    //根据查询删除
    @Test
    public void deleteDocumentByQuery()throws Exception{
        SolrServer solrServer=new HttpSolrServer("http://192.168.169.130:8080/solr/collection1");
        solrServer.deleteByQuery("id:test002");  //表示删除id为test002的文档
        solrServer.commit();
    }

    //搜索
    @Test
    public void SearchDocument() throws Exception {
        //创建一个SolrServer对象
        SolrServer solrServer=new HttpSolrServer("http://192.168.169.130:8080/solr/collection1");
        //创建一个SolrQuery对象
        SolrQuery solrQuery=new SolrQuery();
        //设置查询条件，过滤田间，分页条件，排序条件，高亮
        solrQuery.set("q","手机");
        //分页
        solrQuery.setStart(0);
        solrQuery.setRows(3);
        //设置默认搜索域
        solrQuery.set("df","item_keywords");
        //设置高亮
        solrQuery.setHighlight(true);
        //高亮显示的域
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em>");
        solrQuery.setHighlightSimplePost("</em>");
        //执行查询，得到一个response对象
        QueryResponse response= solrServer.query(solrQuery);
        //取查询结果
        SolrDocumentList solrDocumentList= response.getResults();
        //取查询结果总记录数
        System.out.println("查询结果总记录数："+solrDocumentList.getNumFound());
        for (SolrDocument solrDocument: solrDocumentList){
            System.out.println( solrDocument.get("id"));
            //取高亮显示
            Map<String,Map<String, List<String>>> highlighting=response.getHighlighting();
            List<String> list =highlighting.get(solrDocument.get("id")).get("item_title");
            String itemtitle="";
            if (list !=null && list.size()>0){
                itemtitle=list.get(0);
            }
            else{
                itemtitle=(String)solrDocument.get("item_title");
            }
            System.out.println( itemtitle);
            System.out.println( solrDocument.get("item_sell_point"));
            System.out.println( solrDocument.get("item_price"));
            System.out.println( solrDocument.get("item_image"));
            System.out.println( solrDocument.get("item_category_name"));
            System.out.println( "=================================");
        }


    }
}
