package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ContentCategoryService {

    List<EasyUITreeNode>  getContentCategoryList(long parentId);
    TaotaoResult addContentCategory(Long parentId,String name);
    void updateContentCategory(Long id,String name);
    void deleteContentCategory(Long id);

}
