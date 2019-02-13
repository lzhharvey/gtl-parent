package com.taotao.order.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.jedis.JedisClient;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ORDER_ID_GEN}")
    private String ORDER_ID_GEN;
    @Value("${ORDER_ID_BEGIN_VALUE}")
    private String ORDER_ID_BEGIN_VALUE;
    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;

    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
        //生成订单号,可以使用redis的incr生成
        if (!jedisClient.exists(ORDER_ID_GEN)){
            //设置初始值
            jedisClient.set(ORDER_ID_GEN,ORDER_ID_BEGIN_VALUE);
        }
        String orderId = jedisClient.incr(ORDER_ID_GEN).toString();
        //向订单表插入数据，需要补全pojo的属性
        orderInfo.setOrderId(orderId);
        //免邮费
        orderInfo.setPostFee("0");
        //1.未付款 2.已经付款 3.未发货 4.已经发货 5.交易成功 6.交易关闭
        orderInfo.setStatus(1);
        //订单创建时间
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //向订单表插入数据
        tbOrderMapper.insert(orderInfo);
        //向订单明细表插入数据。
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            //获得主键明细
            String oid = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString();
            orderItem.setId(oid);
            orderItem.setOrderId(orderId);
            //插入明细数据
            tbOrderItemMapper.insert(orderItem);
        }
        // 向订单物流表插入数据
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        tbOrderShippingMapper.insert(orderShipping);
        //返回订单号
        return TaotaoResult.ok(orderId);
    }
}
