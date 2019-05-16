package com.gtl.service.impl;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.gtl.jedis.JedisClient;
import com.gtl.pojo.TbItemDescExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.common.utils.IDUtils;
import com.gtl.mapper.TbItemDescMapper;
import com.gtl.mapper.TbItemMapper;
import com.gtl.pojo.TbItem;
import com.gtl.pojo.TbItemDesc;
import com.gtl.pojo.TbItemExample;
import com.gtl.service.ItemService;

import javax.annotation.Resource;
import javax.jms.*;


/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="itemAddOrUpdatetopic")
	private Destination destination;
	@Resource(name="itemDeletetopic")
	private Destination itemDeletetopic;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	@Value("${TIME_EXPIRE}")
	private Integer TIME_EXPIREl;

	@Override
	public TbItem getItemById(long itemId) {
		//查询数据库之前先查询缓存
		try{
			String s = jedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
			if(StringUtils.isNotBlank(s)){
				//把json数据转成pojo
				TbItem tbItem=JSON.parseObject(s,TbItem.class);
				return tbItem;
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		//缓存中没有查询数据库
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		try {
			//把查询结果添加到缓存
			jedisClient.set(ITEM_INFO + ":" + itemId + ":BASE", JSON.toJSONString(item));
			//设置过期时间，提高缓存利用率
			jedisClient.expire(ITEM_INFO + ":" + itemId + ":BASE",TIME_EXPIREl);
		}catch(Exception  e){
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//取查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		//返回结果
		return result;
	}

	@Override
	public gtlResult addItem(TbItem item, String desc) {
		//生成商品id
		final long itemId = IDUtils.genItemId();
		//补全item的属性
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表对应的pojo
		TbItemDesc itemDesc = new TbItemDesc();
		//补全pojo的属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		itemDesc.setCreated(new Date());
		//向商品描述表插入数据
		itemDescMapper.insert(itemDesc);

		//向Activemq发送商品添加消息
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				//发送商品id
				TextMessage textMessage=session.createTextMessage(itemId+"");
				return textMessage;
			}
		});

		//同步缓存
		//删除对应的缓存信息
		jedisClient.del(ITEM_INFO+ ":item");

		//返回结果
		return gtlResult.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//查询数据库之前先查询缓存
		try{
			String s = jedisClient.get(ITEM_INFO + ":" + itemId + ":DESC");
			if(StringUtils.isNotBlank(s)){
				//把json数据转成pojo
				TbItemDesc tbItemDesc=JSON.parseObject(s,TbItemDesc.class);
				return tbItemDesc;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		//缓存没有查询数据库
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		try {
			//把查询结果添加到缓存
			jedisClient.set(ITEM_INFO + ":" + itemId + ":DESC", JSON.toJSONString(tbItemDesc));
			//设置过期时间，提高缓存利用率
			jedisClient.expire(ITEM_INFO + ":" + itemId + ":DESC",TIME_EXPIREl);
		}catch(Exception  e){
			e.printStackTrace();
		}
		return tbItemDesc;
	}

	@Override
	public List<TbItem> getItem(TbItem tbItem) {
		TbItemExample tbItemExample=new TbItemExample();
		TbItemExample.Criteria criteria = tbItemExample.createCriteria();
		if (tbItem.getId()!=null){
		criteria.andIdEqualTo(tbItem.getId());
		}
		if (tbItem.getTitle()!=null){
		criteria.andTitleEqualTo(tbItem.getTitle());
		}
		if (tbItem.getSellPoint()!=null) {
			criteria.andSellPointEqualTo(tbItem.getSellPoint());
		}
		if (tbItem.getPrice()!=null) {
		criteria.andPriceEqualTo(tbItem.getPrice());
		}
		if (tbItem.getNum()!=null) {
		criteria.andNumEqualTo(tbItem.getNum());
		}
		if (tbItem.getBarcode()!=null) {
		criteria.andBarcodeEqualTo(tbItem.getBarcode());
		}
		if (tbItem.getImage()!=null) {
		criteria.andImageEqualTo(tbItem.getImage());
		}
		if (tbItem.getCid()!=null) {
		criteria.andCidEqualTo(tbItem.getCid());
		}
		if (tbItem.getStatus()!=null) {
		criteria.andStatusEqualTo(tbItem.getStatus());
		}
		if (tbItem.getCreated()!=null) {
		criteria.andCreatedEqualTo(tbItem.getCreated());
		}
		if (tbItem.getUpdated()!=null) {
		criteria.andUpdatedEqualTo(tbItem.getUpdated());
		}
		//查询数据库之前先查询缓存
		if (tbItem.getId()!=null){
		try{
			String s = jedisClient.get(ITEM_INFO+":"+tbItem.getId()+ ":item");
			if(StringUtils.isNotBlank(s)){
				//把json数据转成pojo
				List<TbItem> tbItemList = JSON.parseArray(s, TbItem.class);
				return tbItemList;
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		}
		//查询数据库
		List<TbItem> tbItems = itemMapper.selectByExample(tbItemExample);
		//并把数据添加到缓存
		try {
			for (TbItem t: tbItems) {
				//把查询结果添加到缓存
				jedisClient.set(ITEM_INFO + ":" + t.getId() + ":item", JSON.toJSONString(tbItems));
				//设置过期时间，提高缓存利用率
				jedisClient.expire(ITEM_INFO + ":" + t.getId() + ":item", TIME_EXPIREl);
			}
			}catch(Exception  e){
			e.printStackTrace();
		}

		return tbItems;
	}

	/**
	 * 删除
	 * @param ids
	 * @return gtlResult
	 */
	@Override
	public gtlResult deleteItem(final List<Long> ids) {
		for (final Long l: ids){
			TbItemExample tbItemExample=new TbItemExample();
			//设置查询条件
			TbItemExample.Criteria criteria = tbItemExample.createCriteria();
			criteria.andIdEqualTo(l);
			//执行删除
			try{
				itemMapper.deleteByExample(tbItemExample);
				//向Activemq发送商品添加消息
				jmsTemplate.send(itemDeletetopic, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						//发送商品id
						TextMessage textMessage=session.createTextMessage(l+"");
						return textMessage;
					}
				});
				//同步缓存
				//删除对应的缓存信息
				jedisClient.del(ITEM_INFO+ ":item");


			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		return gtlResult.ok();
	}

	/**
	 * 更新
	 * @param item
	 * @return gtlResult
	 */
	@Override
	public gtlResult updateItem(TbItem item,String desc) {
		//设置更新条件
		try {
			// 1、根据商品id，更新商品表，条件更新
			TbItemExample itemExample = new TbItemExample();
			TbItemExample.Criteria criteria = itemExample.createCriteria();
			criteria.andIdEqualTo(item.getId());
			itemMapper.updateByExampleSelective(item, itemExample);

			// 2、根据商品id，更新商品描述表，条件更新
			TbItemDesc itemDesc = new TbItemDesc();
			itemDesc.setItemDesc(desc);
			TbItemDescExample itemDescExample = new TbItemDescExample();
			com.gtl.pojo.TbItemDescExample.Criteria createCriteria =itemDescExample.createCriteria();
			createCriteria.andItemIdEqualTo(item.getId());
			itemDescMapper.updateByExampleSelective(itemDesc, itemDescExample);

			//向Activemq发送商品添加消息
			final String id =item.getId()+"";
			jmsTemplate.send(destination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					//发送商品id
					TextMessage textMessage=session.createTextMessage(id);
					return textMessage;
				}
			});
			//同步缓存
			//删除对应的缓存信息
			jedisClient.del(ITEM_INFO+ ":item");

		}
		catch (Exception e){
			e.printStackTrace();
		}
		return gtlResult.ok();
	}
}
