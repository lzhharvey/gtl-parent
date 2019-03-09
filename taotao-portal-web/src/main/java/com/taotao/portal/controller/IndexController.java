package com.taotao.portal.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;
import com.taotao.portal.utils.IndexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页展示
 */
@Controller
public class IndexController {
    //轮播
    @Value("${AD1_CATEGORY_ID}")
    private  Long AD1_CATEGORY_ID;
    @Value("${AD1_WIDTH}")
    private  Integer AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")
    private  Integer AD1_WIDTH_B;
    @Value("${AD1_HEIGHT}")
    private  Integer AD1_HEIGHT;
    @Value("${AD1_HEIGHT_B}")
    private  Integer AD1_HEIGHT_B;

    //右上角广告
    @Value("${AD1_ysfxgg}")
    private Long AD1_ysfxgg;
    @Value("${AD1_ysfxgg_WIDTH}")
    private Integer AD1_ysfxgg_WIDTH;
    @Value("${AD1_ysfxgg_WIDTH_B}")
    private Integer AD1_ysfxgg_WIDTH_B;
    @Value("${AD1_ysfxgg_HEIGHT}")
    private Integer AD1_ysfxgg_HEIGHT;
    @Value("${AD1_ysfxgg_HEIGHT_B}")
    private Integer AD1_ysfxgg_HEIGHT_B;

    //快报
    @Value("${AD2_KB}")
    private Long AD2_KB;


    @Autowired
    private  ContentService contentService;

    //返回的"index",不能使用@ResponeBody注解，不然无法返回jsp页面
    @RequestMapping("/index")
    public String showIndex(Model mode){
        String json;
        //轮播
        json=IndexUtil.getContent(contentService,AD1_CATEGORY_ID,AD1_HEIGHT,AD1_HEIGHT_B,AD1_WIDTH,AD1_WIDTH_B);
        //把json数据传到页面
        mode.addAttribute("ad1",json);

        //右上方广告
        //把列表转换成json数据
        json=IndexUtil.getContent(contentService,AD1_ysfxgg,AD1_ysfxgg_HEIGHT,AD1_ysfxgg_HEIGHT_B,AD1_ysfxgg_WIDTH,AD1_ysfxgg_WIDTH_B);
        //把json数据传到页面
        mode.addAttribute("ad",json);

        //快报  返回的是list,不是json了
        List<TbContent> list=IndexUtil.getContent(contentService,AD2_KB);
        //把json数据传到页面
        mode.addAttribute("ad2",list);




        return "index";
    }


}
