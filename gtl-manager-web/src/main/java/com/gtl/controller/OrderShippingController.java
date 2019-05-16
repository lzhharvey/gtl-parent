package com.gtl.controller;


import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.order.service.OrderShippingService;
import com.gtl.pojo.TbOrderItem;
import com.gtl.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrderShippingController {
    @Autowired
    private OrderShippingService orderShippingService;


    @RequestMapping("/orderShipping/list")
    @ResponseBody
    public EasyUIDataGridResult getOrderItemList(Integer page , Integer rows){
        EasyUIDataGridResult orderList = orderShippingService.getOrderShippingList(page,rows);
        return orderList;
    }

    @RequestMapping("/orderShipping/delete")
    @ResponseBody
    public gtlResult deleteOrder(@RequestParam List<Long> ids){
        gtlResult g = orderShippingService.deleteOrderShipping(ids);
        return g.ok();
    }

    @RequestMapping("/orderShipping/update")
    @ResponseBody
    public  gtlResult updateOrder(TbOrderShipping tbOrderShipping){
        gtlResult g = orderShippingService.updateOrderShipping(tbOrderShipping);
        return g.ok();
    }
    
    
}
