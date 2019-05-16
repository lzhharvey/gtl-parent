package com.gtl.controller;

import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.order.service.OrderItemService;
import com.gtl.pojo.TbOrder;
import com.gtl.pojo.TbOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @RequestMapping("/orderItem/list")
    @ResponseBody
    public EasyUIDataGridResult getOrderItemList(Integer page ,Integer rows){
        EasyUIDataGridResult orderList = orderItemService.getOrderItemList(page,rows);
        return orderList;
    }

    @RequestMapping("/orderItem/delete")
    @ResponseBody
    public gtlResult deleteOrder(@RequestParam List<Long> ids){
        gtlResult g = orderItemService.deleteOrderItem(ids);
        return g.ok();
    }

    @RequestMapping("/orderItem/update")
    @ResponseBody
    public  gtlResult updateOrder(TbOrderItem tbOrderItem){
        gtlResult g = orderItemService.updateOrderItem(tbOrderItem);
        return g.ok();
    }

}
