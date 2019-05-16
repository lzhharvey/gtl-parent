package com.gtl.portal.utils;

import com.gtl.common.utils.JsonUtils;
import com.gtl.content.service.ContentService;
import com.gtl.pojo.TbContent;
import com.gtl.portal.pojo.AD1Node;

import java.util.ArrayList;
import java.util.List;

public class IndexUtil {

    public static String getContent(ContentService contentService, Long id, Integer height, Integer height_b, Integer width, Integer width_b){
        //根据cid查询内容列表
        List<TbContent> list=contentService.getContentByCid(id);
        //把列表转换成AD1Node列表
        List<AD1Node> ad1Nodes=new ArrayList<>();
        for (TbContent tbContent: list){
            AD1Node ad1Node=new AD1Node();
            ad1Node.setAlt(tbContent.getTitle());
            ad1Node.setHeight(height);
            ad1Node.setHeightB(height_b);
            ad1Node.setWidth(width);
            ad1Node.setWidthB(width_b);
            ad1Node.setSrc(tbContent.getPic());
            ad1Node.setSrcB(tbContent.getPic2());
            ad1Node.setHref(tbContent.getUrl());
            //添加到节点列表
            ad1Nodes.add(ad1Node);
        }
        //把列表转换成json数据
        return  JsonUtils.objectToJson(ad1Nodes);
    }

    public static List<TbContent> getContent(ContentService contentService, Long id){
        //根据cid查询内容列表
        List<TbContent> list=contentService.getContentByCid(id);
        //把列表转换成json数据
        return  list;
    }

}
