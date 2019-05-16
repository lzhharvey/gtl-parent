package com.gtl.portal.controller;

import com.gtl.content.service.ContentService;
import com.gtl.pojo.TbContent;
import com.gtl.pojo.TbItem;
import com.gtl.portal.utils.IndexUtil;
import com.gtl.service.ItemCatService;
import com.gtl.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 首页展示
 */
@Controller
public class IndexController {
    //轮播
    @Value("${AD1_CATEGORY_ID}")
    private Long AD1_CATEGORY_ID;
    @Value("${AD1_WIDTH}")
    private Integer AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")
    private Integer AD1_WIDTH_B;
    @Value("${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;
    @Value("${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;

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

    //楼层
    @Value("${AD2_KB}")
    private Long AD3_LC_ID;
    @Value("${AD3_IsParent}")
    private String AD3_IsParent;
    @Value("${AD3_Str1}")
    private String AD3_Str1;
    @Value("${AD3_Str2}")
    private String AD3_Str2;
    @Value("${AD3_Str3}")
    private String AD3_Str3;
    @Value("${AD3_Str4}")
    private String AD3_Str4;

    @Autowired
    private ContentService contentService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemCatService itemCatService;

    //返回的"index",不能使用@ResponeBody注解，不然无法返回jsp页面
    @RequestMapping("/index")
    public String showIndex(Model mode) {
        String json;
        //轮播==========================================================================================================
        json = IndexUtil.getContent(contentService, AD1_CATEGORY_ID, AD1_HEIGHT, AD1_HEIGHT_B, AD1_WIDTH, AD1_WIDTH_B);
        //把json数据传到页面
        mode.addAttribute("ad1", json);

        //右上方广告====================================================================================================
        //把列表转换成json数据
        json = IndexUtil.getContent(contentService, AD1_ysfxgg, AD1_ysfxgg_HEIGHT, AD1_ysfxgg_HEIGHT_B, AD1_ysfxgg_WIDTH, AD1_ysfxgg_WIDTH_B);
        //把json数据传到页面
        mode.addAttribute("ad", json);
//
        //快报  返回的是list,不是json了=================================================================================
        List<TbContent> list = IndexUtil.getContent(contentService, AD2_KB);
        //把json数据传到页面
        mode.addAttribute("ad2", list);
//
//        //楼层==========================================================================================================
//        //1.随机获取商品分类,isparent为0的
//        List<AD3Node> ad3NodeList=new ArrayList<>();
//        List<List<TbItemCat>> list1=new ArrayList();
//        String[] strings={AD3_Str1,AD3_Str2,AD3_Str3,AD3_Str4};
//        for (int j=0;j<=3;j++){
//            //封装属性
//            TbItemCat tbItemCat = new TbItemCat();
//            tbItemCat.setName(strings[j]);
//            if (!Boolean.valueOf(AD3_IsParent)) {
//                tbItemCat.setIsParent(false);
//            }
//            //查询
//            List<TbItemCat> itemCat = itemCatService.getItemCat(tbItemCat);
//            //判断是否查询出了数据
//            if(itemCat.size()==1){
//                list1.add(itemCat);
//            }
//        }
//        //遍历list1
//        List<List<TbItem>> item=new ArrayList();
//        for ( List<TbItemCat> tbItemCats: list1){
//            for (TbItemCat tbItemCat : tbItemCats){
//                //2.根据商品分类id,获取对应的所有商品
//                TbItem tbItem=new TbItem();
//                tbItem.setCid(tbItemCat.getId());
//                List<TbItem> tbItemList = itemService.getItem(tbItem);
//                item.add(tbItemList);
//                //将分类名放入ad3NodeList
//                AD3Node ad3Node=new AD3Node();
//                ad3Node.setTitle(tbItemCat.getName());
//                ad3NodeList.add(ad3Node);
//            }
//        }
//        //遍历item
//        int z=0;
//        for (List<TbItem> list2: item){
//            //将商品信息放入ad3NodeList
//            ad3NodeList.get(z).setTbItems(list2);
//            z++;
//        }
//        //将分类名和对应的商品传至前端
//        mode.addAttribute("ad3",ad3NodeList);

        return "index";
    }





}
