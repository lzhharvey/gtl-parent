package com.gtl.content.service;

import com.gtl.common.pojo.EasyUITreeNode;
import com.gtl.common.pojo.gtlResult;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ContentCategoryService {

    List<EasyUITreeNode>  getContentCategoryList(long parentId);
    gtlResult addContentCategory(Long parentId,String name);
    void updateContentCategory(Long id,String name);
    void deleteContentCategory(Long id);

}
