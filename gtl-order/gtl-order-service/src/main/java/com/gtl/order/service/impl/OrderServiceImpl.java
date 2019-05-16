package com.gtl.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.mapper.TbOrderItemMapper;
import com.gtl.mapper.TbOrderMapper;
import com.gtl.mapper.TbOrderShippingMapper;
import com.gtl.order.jedis.JedisClient;
import com.gtl.order.pojo.OrderInfo;
import com.gtl.order.service.OrderService;
import com.gtl.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    public gtlResult createOrder(OrderInfo orderInfo) {
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
        return gtlResult.ok(orderId);
    }

    /**
     * 订单列表
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUIDataGridResult getOrderList(int page, int rows) {

        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbOrderExample example = new TbOrderExample();
        List<TbOrder> list = tbOrderMapper.selectByExample(example);
        //取查询结果
        PageInfo<TbOrder> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        //返回结果
        return result;
    }

    /**
     * 订单删除
     * @param ids
     * @return
     */
    @Override
    public gtlResult deleteOrder(List<Long> ids) {

        for (Long l: ids) {
            //删除订单，对应的订单项和订单shipping也应该删除
             tbOrderMapper.deleteByPrimaryKey(l + "");
             //删除订单shipping
            tbOrderShippingMapper.deleteByPrimaryKey(l+"");
            //删除订单item
            TbOrderItemExample tbOrderItemExample=new TbOrderItemExample();
            TbOrderItemExample.Criteria criteria = tbOrderItemExample.createCriteria();
            criteria.andOrderIdEqualTo(l+"");
            tbOrderItemMapper.deleteByExample(tbOrderItemExample);

        }
        return gtlResult.ok();
    }

    /**
     * 订单更新 主动修改邮费，自动更新订单价和时间
     * @param order
     * @return
     */
    @Override
    public gtlResult updateOrder(TbOrder order) {
        //设置更新条件
        try {
            // 1、根据订单id，更新订单表，条件更新
            TbOrderExample orderExample = new TbOrderExample();
            TbOrderExample.Criteria criteria = orderExample.createCriteria();
            criteria.andOrderIdEqualTo(order.getOrderId());
//            //获取之前邮费
//            TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(order.getOrderId());
//            String postFee = tbOrder.getPostFee();
//            tbOrder.getPayment()
            order.setUpdateTime(new Date());
            tbOrderMapper.updateByExampleSelective(order, orderExample);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return gtlResult.ok();
    }
}
