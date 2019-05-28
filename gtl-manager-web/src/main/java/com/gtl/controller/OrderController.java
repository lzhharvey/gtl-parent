package com.gtl.controller;

import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.order.service.OrderService;
import com.gtl.pojo.TbOrder;
import com.gtl.pojo.TbOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/list")
    @ResponseBody
    public EasyUIDataGridResult getOrderList(Integer page,Integer rows){

        EasyUIDataGridResult orderList = orderService.getOrderList(page, rows);



        return orderList;
    }

    @RequestMapping("/order/delete")
    @ResponseBody
    public gtlResult deleteOrder(@RequestParam List<Long> ids){
        gtlResult g = orderService.deleteOrder(ids);
        return g.ok();
    }

    @RequestMapping("/order/update")
    @ResponseBody
    public  gtlResult updateOrder(TbOrder tbOrder){
        gtlResult g = orderService.updateOrder(tbOrder);
        return g.ok();
    }

}
