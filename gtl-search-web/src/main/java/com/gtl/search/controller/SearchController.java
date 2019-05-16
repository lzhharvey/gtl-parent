package com.gtl.search.controller;

/**
 * 搜索服务controller
 */

import com.gtl.common.pojo.SearchResult;
import com.gtl.pojo.TbItem;
import com.gtl.search.service.SearchService;
import com.gtl.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private ItemService itemService;
    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @RequestMapping("/search")
    public  String search(@RequestParam("q") String queryString, @RequestParam(defaultValue = "1") Integer page, Model model){
        try {
            //把查询条件进行转码，解决get乱码问题
            queryString=new String(queryString.getBytes("iso8859-1"),"utf-8");
            SearchResult searchResult=searchService.search(queryString,page,SEARCH_RESULT_ROWS);
            //把结果传递给页面
            model.addAttribute("query",queryString);
            model.addAttribute("totalPages",searchResult.getTotalPages());
            model.addAttribute("itemList",searchResult.getItemList());
            model.addAttribute("page",page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回逻辑视图
        return "search";
    }

    //根据cid查商品
//    @RequestMapping("searchByCid")
//    public String selectByCid(@RequestParam("cid")String  name, @RequestParam(defaultValue = "1") Integer page, Model model){
//
//        SearchResult searchResult=searchService.search(item.getCid()+"",page,SEARCH_RESULT_ROWS);
//    }

}
