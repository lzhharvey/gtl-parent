package com.gtl.content.service;

import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.pojo.TbContent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentService {

    EasyUIDataGridResult getContentList(Long categoryId, int page, int rows);

    gtlResult addContent(TbContent content);

    gtlResult  updateContent(TbContent content);

    gtlResult deleteContent(List<Long> list);

    /**
     * 根据分类id，获取内容列表
     * @param cid
     * @return
     */
    List<TbContent> getContentByCid(long cid);

}
