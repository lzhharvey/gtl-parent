package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentService {

    EasyUIDataGridResult getContentList(Long categoryId, int page, int rows);

    TaotaoResult addContent(TbContent content);

    TaotaoResult  updateContent(TbContent content);

    TaotaoResult deleteContent(List<Long> list);

    /**
     * 根据分类id，获取内容列表
     * @param cid
     * @return
     */
    List<TbContent> getContentByCid(long cid);

}
