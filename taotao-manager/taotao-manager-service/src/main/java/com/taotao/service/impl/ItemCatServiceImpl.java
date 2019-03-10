package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.service.ItemCatService;

/**
 * 商品分类管理Service
 * <p>Title: ItemCatServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		//根据父节点id查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		//设置查询条件
		TbItemCatExample.Criteria criteria = example.createCriteria();
		//设置parentid
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//转换成EasyUITreeNode列表
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			//如果节点下有子节点“closed”，如果没有子节点“open”
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			//添加到节点列表
			resultList.add(node);
		}
		return resultList;
	}

	@Override
	public List<TbItemCat> getItemCat(TbItemCat tbItemCat) {

		TbItemCatExample tbItemCatExample=new TbItemCatExample();
		TbItemCatExample.Criteria criteria = tbItemCatExample.createCriteria();
		if (tbItemCat.getId()!=null){
		criteria.andIdEqualTo(tbItemCat.getId());
		}
		if (tbItemCat.getIsParent()!=null) {
		criteria.andIsParentEqualTo(tbItemCat.getIsParent());
		}
		if (tbItemCat.getId()!=null) {
			criteria.andIdEqualTo(tbItemCat.getId());
		}
		if (tbItemCat.getParentId()!=null) {
			criteria.andParentIdEqualTo(tbItemCat.getParentId());
		}
		if (tbItemCat.getName()!=null) {
			criteria.andNameEqualTo(tbItemCat.getName());
		}
		if (tbItemCat.getStatus()!=null) {
			criteria.andStatusEqualTo(tbItemCat.getStatus());
		}
		if (tbItemCat.getSortOrder()!=null) {
			criteria.andSortOrderEqualTo(tbItemCat.getSortOrder());
		}
		if (tbItemCat.getCreated()!=null) {
			criteria.andCreatedEqualTo(tbItemCat.getCreated());
		}
		if (tbItemCat.getUpdated()!=null) {
			criteria.andUpdatedEqualTo(tbItemCat.getUpdated());
		}
		List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(tbItemCatExample);
		return tbItemCats;
	}
}
