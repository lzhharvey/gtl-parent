package com.gtl.order.service;

import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.pojo.TbOrderItem;

import java.util.List;

public interface OrderItemService {
    gtlResult deleteOrderItem(List<Long> ids);

    EasyUIDataGridResult getOrderItemList(int page, int rows) ;

    gtlResult updateOrderItem(TbOrderItem tbOrderItem) ;

}
