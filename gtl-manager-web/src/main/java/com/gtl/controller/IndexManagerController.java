package com.gtl.controller;

import com.gtl.common.pojo.gtlResult;
import com.gtl.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *索引库维护
 */
@Controller
public class IndexManagerController {
    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/index/import")
    @ResponseBody
    public gtlResult importIndex(){
    gtlResult gtlResult=searchItemService.importItemToIndex();
    return gtlResult;
    }


}
