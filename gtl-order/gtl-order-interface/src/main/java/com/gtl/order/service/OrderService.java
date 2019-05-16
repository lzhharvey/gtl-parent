package com.gtl.order.service;

import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.order.pojo.OrderInfo;
import com.gtl.pojo.TbOrder;

import java.util.List;

public interface OrderService {

    gtlResult createOrder(OrderInfo orderInfo);

    gtlResult  deleteOrder(List<Long> ids);

    EasyUIDataGridResult getOrderList(int page, int rows) ;

    gtlResult updateOrder(TbOrder order) ;

    }
