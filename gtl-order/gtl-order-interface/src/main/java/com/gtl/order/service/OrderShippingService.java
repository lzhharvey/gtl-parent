package com.gtl.order.service;

import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.pojo.TbOrderShipping;

import java.util.List;

public interface OrderShippingService {
    gtlResult deleteOrderShipping(List<Long> ids);

    EasyUIDataGridResult getOrderShippingList(int page, int rows) ;

    gtlResult updateOrderShipping(TbOrderShipping tbOrderShipping) ;
}
