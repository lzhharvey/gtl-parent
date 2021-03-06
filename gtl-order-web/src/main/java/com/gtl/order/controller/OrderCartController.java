package com.gtl.order.controller;

import com.alibaba.fastjson.JSON;
import com.gtl.common.pojo.gtlResult;
import com.gtl.common.utils.CookieUtils;
import com.gtl.order.pojo.OrderInfo;
import com.gtl.order.service.OrderService;
import com.gtl.pojo.TbItem;
import com.gtl.pojo.TbUser;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单确认页面处理Controller
 */
@Controller
public class OrderCartController {
    @Value("${CART_KEY}")
    private String CART_KEY;
    @Autowired
    private OrderService orderService;

    /**
     * 展示订单确认页面
     */
    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request){
        //用户必须是登陆状态
        //取用户id
        TbUser user = (TbUser) request.getAttribute("user");
        System.out.println(user.getUsername());
        //根据用户信息取收货地址
        //把收货地址列表取出来传递给页面
        //从cookie中取购物车商品列表展示到页面
        List<TbItem> cartItemList = getCartItemList(request);
        //返回逻辑视图
        request.setAttribute("cartList",cartItemList);
        return "order-cart";
    }
    private List<TbItem> getCartItemList(HttpServletRequest request){
        //从cookie中取购物车商品列表
        String cookieValue = CookieUtils.getCookieValue(request, CART_KEY, true);
        if(StringUtils.isBlank(cookieValue)){
            return new ArrayList<>();
        }
        List<TbItem> list= JSON.parseArray(cookieValue,TbItem.class);
        return list;
    }

    /**
     * 生成的订单的处理
     */
    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, Model model){
        //生成订单
        gtlResult result = orderService.createOrder(orderInfo);
        //返回逻辑视图
        model.addAttribute("orderId", result.getData().toString());
        model.addAttribute("payment",orderInfo.getPayment());
        //预计送达时间，预计三天后送达
        DateTime dateTime=new DateTime();
        dateTime = dateTime.plusDays(3);
        model.addAttribute("date",dateTime.toString("yyyy-MM-dd"));
        return "success";
    }


}
