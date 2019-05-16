package com.gtl.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.mapper.TbOrderShippingMapper;
import com.gtl.order.service.OrderShippingService;
import com.gtl.pojo.TbOrderShipping;
import com.gtl.pojo.TbOrderShippingExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class OrderShippingServiceImpl implements OrderShippingService {
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Override
    public gtlResult deleteOrderShipping(List<Long> ids) {
        for (Long l: ids) {
            //删除订单物流
            TbOrderShippingExample tbOrderShippingExample=new TbOrderShippingExample();
            TbOrderShippingExample.Criteria criteria = tbOrderShippingExample.createCriteria();
            criteria.andOrderIdEqualTo(l+"");
            int i= tbOrderShippingMapper.deleteByExample(tbOrderShippingExample);
        }
        return gtlResult.ok();
    }

    @Override
    public EasyUIDataGridResult getOrderShippingList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbOrderShippingExample tbOrderItemExample=new TbOrderShippingExample();
        List<TbOrderShipping> tbOrderItems = tbOrderShippingMapper.selectByExample(tbOrderItemExample);

        //取查询结果
        PageInfo<TbOrderShipping> pageInfo=new PageInfo<>(tbOrderItems);
        EasyUIDataGridResult easyUIDataGridResult=new EasyUIDataGridResult();
        easyUIDataGridResult.setTotal(pageInfo.getTotal());
        easyUIDataGridResult.setRows(tbOrderItems);

        return easyUIDataGridResult;
    }

    @Override
    public gtlResult updateOrderShipping(TbOrderShipping tbOrderShipping) {
        //设置更新条件
        try {
            // 1、根据订单id，更新订单表，条件更新
            TbOrderShippingExample tbOrderShippingExample = new TbOrderShippingExample();
            TbOrderShippingExample.Criteria criteria = tbOrderShippingExample.createCriteria();
            criteria.andOrderIdEqualTo(tbOrderShipping.getOrderId());

            //修改时间也要同时更新
            tbOrderShipping.setUpdated(new Date());
            tbOrderShippingMapper.updateByExampleSelective(tbOrderShipping, tbOrderShippingExample);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return gtlResult.ok();
    }
}
