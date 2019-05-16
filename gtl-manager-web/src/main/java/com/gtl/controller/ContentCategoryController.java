package com.gtl.controller;

import com.gtl.common.pojo.EasyUITreeNode;
import com.gtl.common.pojo.gtlResult;
import com.gtl.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理Controller
 */
@Controller
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value = "id",defaultValue ="0" ) Long parentId){
        List<EasyUITreeNode> list=contentCategoryService.getContentCategoryList(parentId);
        for (EasyUITreeNode easyUITreeNode : list) {
            System.out.println(easyUITreeNode.getText()+" "+easyUITreeNode.getId());
        }
        return list;
    }

    @RequestMapping("/content/category/create")
    @ResponseBody
    public gtlResult addContentCategory(Long parentId,String name){
        gtlResult result=contentCategoryService.addContentCategory(parentId,name);
        return  result;
    }

    @RequestMapping("/content/category/update")
    @ResponseBody
    public void updateContentCategory(Long id ,String name){
        contentCategoryService.updateContentCategory(id,name);
    }

    @RequestMapping("/content/category/delete/")
    @ResponseBody
    public void deleteContentCategory(Long id){
        contentCategoryService.deleteContentCategory(id);
    }


}
