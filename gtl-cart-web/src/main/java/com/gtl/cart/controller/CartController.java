package com.gtl.cart.controller;

import com.alibaba.fastjson.JSON;
import com.gtl.common.pojo.gtlResult;
import com.gtl.common.utils.CookieUtils;
import com.gtl.pojo.TbItem;
import com.gtl.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车管理Controller
 */
@Controller
public class CartController {

    @Value("${CART_KEY}")
    private String CART_KEY;
    @Value("${CART_EXPIER}")
    private Integer CART_EXPIER;
    @Autowired
    private ItemService itemService;

    @RequestMapping("/cart/add/{itemId}")
    public String addItemCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1")Integer num, HttpServletRequest request, HttpServletResponse response){
        //取购物车列表
        List<TbItem> cartItemList = getCartItemList(request);
        //判断商品在购物车中是否存在
        boolean flag=false;
        for (TbItem tbItem : cartItemList) {
            //这里两个long对象进行比较，比较的是地址值
            //要想比较的是内容，得加longValue()方法
            if (tbItem.getId()==itemId.longValue()){
                //如果存在数量相加
                tbItem.setNum(tbItem.getNum()+num);
                flag=true;
                break;
            }
        }
        //如果不存在，添加一个新的商品
        if(!flag){
            //需要添加一个新的商品
            TbItem item = itemService.getItemById(itemId);
            //设置购买的数量
            item.setNum(num);
            //取一张图片
            String image = item.getImage();
            if(StringUtils.isNotBlank(image)){
                String[] images=image.split(",");
                item.setImage(images[0]);
            }
            //把商品添加到购物车
            cartItemList.add(item);
        }
        //把购物车列表写入cookie
//        CookieUtils.setCookie(request,response,CART_KEY, JSON.toJSONString(cartItemList),CART_EXPIER,true);
        try {
            Cookie cookie = new Cookie(CART_KEY, URLEncoder.encode(JSON.toJSONString(cartItemList), "utf-8"));
            cookie.setDomain("localhost");
            cookie.setPath("/");
            cookie.setMaxAge(CART_EXPIER);
            response.addCookie(cookie);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //返回添加成功页面
        return "cartSuccess";
    }
    private List<TbItem> getCartItemList(HttpServletRequest request){
        //从cookie中取购物车商品列表
        String cookieValue = CookieUtils.getCookieValue(request, CART_KEY, true);
        if(StringUtils.isBlank(cookieValue)){
            return new ArrayList<>();
        }
        List<TbItem> list=JSON.parseArray(cookieValue,TbItem.class);
        return list;
    }

    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request){
        //从cookie中取购物车列表
        List<TbItem> cartItemList = getCartItemList(request);
        //把购物车列表传递给jsp
        request.setAttribute("cartList",cartItemList);
        //返回逻辑视图
        return "cart";
    }

    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public gtlResult updateItemNum(@PathVariable Long itemId,@PathVariable Integer num, HttpServletRequest request,HttpServletResponse response){
        //从cookie中取购物车列表
        List<TbItem> cartItemList = getCartItemList(request);
        //查询到对应的商品
        for (TbItem tbItem : cartItemList) {
            if (tbItem.getId()==itemId.longValue()){
                //更新商品数量
                tbItem.setNum(num);
                break;
            }
            
        }
        //把购车列表写入cookie
        CookieUtils.setCookie(request,response,CART_KEY, JSON.toJSONString(cartItemList),CART_EXPIER,true);
        //返回成功
        return gtlResult.ok();
    }

    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,HttpServletResponse response,HttpServletRequest request){
        //从cookie中取购物车列表
        List<TbItem> cartItemList = getCartItemList(request);
        //找到对应的商品
        for (TbItem tbItem : cartItemList) {
            if(tbItem.getId()==itemId.longValue()){
                //删除商品
                cartItemList.remove(tbItem);
                break;
            }
        }
        //把购物车列表写入cookie
        try {
            Cookie cookie = new Cookie(CART_KEY, URLEncoder.encode(JSON.toJSONString(cartItemList), "utf-8"));
            cookie.setDomain("localhost");
            cookie.setPath("/");
            cookie.setMaxAge(CART_EXPIER);
            response.addCookie(cookie);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //重定向到购物车列表页面
        return "redirect:/cart/cart.html";
    }
}
