package com.gtl.controller;

import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.content.service.ContentService;
import com.gtl.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 内容管理Controller
 */
@RestController
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/query/list")
    public EasyUIDataGridResult getContentList(@RequestParam(value = "categoryId")Long categoryId,@RequestParam("page")int page,@RequestParam("rows")int rows){
        EasyUIDataGridResult easyUIDataGridResult= contentService.getContentList(categoryId,page,rows);
        return easyUIDataGridResult;
    }

    @RequestMapping("/content/save")
    public gtlResult addContent(TbContent tbContent){
        return contentService.addContent(tbContent);
    }

    @RequestMapping("/rest/content/edit")
    public gtlResult updateContent(TbContent tbContent){
     return contentService.updateContent(tbContent);
    }

    @RequestMapping("/content/delete")
    public gtlResult deleteContent(@RequestParam("ids")List<Long> ids){
        return  contentService.deleteContent(ids);
    }

}
