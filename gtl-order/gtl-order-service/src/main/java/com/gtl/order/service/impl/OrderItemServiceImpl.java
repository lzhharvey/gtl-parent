package com.gtl.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.mapper.TbOrderItemMapper;
import com.gtl.order.service.OrderItemService;
import com.gtl.pojo.TbOrderItem;
import com.gtl.pojo.TbOrderItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderItemServiceImpl  implements OrderItemService {

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;


    @Override
    public gtlResult deleteOrderItem(List<Long> ids) {
        for (Long l: ids) {
            //删除订单item
            TbOrderItemExample tbOrderItemExample=new TbOrderItemExample();
            TbOrderItemExample.Criteria criteria = tbOrderItemExample.createCriteria();
            criteria.andIdEqualTo(l+"");
           int i= tbOrderItemMapper.deleteByExample(tbOrderItemExample);
        }
        return gtlResult.ok();
    }

    @Override
    public EasyUIDataGridResult getOrderItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbOrderItemExample tbOrderItemExample=new TbOrderItemExample();
        List<TbOrderItem> tbOrderItems = tbOrderItemMapper.selectByExample(tbOrderItemExample);

        //取查询结果
        PageInfo<TbOrderItem> pageInfo=new PageInfo<>(tbOrderItems);
        EasyUIDataGridResult easyUIDataGridResult=new EasyUIDataGridResult();
        easyUIDataGridResult.setTotal(pageInfo.getTotal());
        easyUIDataGridResult.setRows(tbOrderItems);

        return easyUIDataGridResult;
    }

    @Override
    public gtlResult updateOrderItem(TbOrderItem tbOrderItem) {
        //设置更新条件
        try {
            // 1、根据订单id，更新订单表，条件更新
            TbOrderItemExample tbOrderItemExample = new TbOrderItemExample();
            TbOrderItemExample.Criteria criteria = tbOrderItemExample.createCriteria();
            criteria.andOrderIdEqualTo(tbOrderItem.getOrderId());

            //更新数量总价也变
            //查询
            TbOrderItem tbOrderItem1 = tbOrderItemMapper.selectByPrimaryKey(tbOrderItem.getId());
            //重新设置总价
            tbOrderItem.setTotalFee(tbOrderItem1.getPrice()*tbOrderItem.getNum());
            tbOrderItemMapper.updateByExampleSelective(tbOrderItem, tbOrderItemExample);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return gtlResult.ok();
    }
}
