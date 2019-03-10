package com.taotao.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.taotao.jedis.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

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
	@Resource(name="itemAddtopic")
	private Destination destination;
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
	public TaotaoResult addItem(TbItem item, String desc) {
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
		return TaotaoResult.ok();
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
		try{
			String s = jedisClient.get(ITEM_INFO+":"+tbItem.getCid()+ ":item");
			if(StringUtils.isNotBlank(s)){
				//把json数据转成pojo
				List<TbItem> tbItemList = JSON.parseArray(s, TbItem.class);
				return tbItemList;
			}

		}catch (Exception e){
			e.printStackTrace();
		}

		//查询数据库
		List<TbItem> tbItems = itemMapper.selectByExample(tbItemExample);
		//并把数据添加到缓存
		try {
			//把查询结果添加到缓存
			jedisClient.set(ITEM_INFO  +":"+tbItem.getCid()+ ":item", JSON.toJSONString(tbItems));
			//设置过期时间，提高缓存利用率
			jedisClient.expire(ITEM_INFO+":"+tbItem.getCid()  + ":item",TIME_EXPIREl);
		}catch(Exception  e){
			e.printStackTrace();
		}

		return tbItems;
	}
}
