package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

import java.util.List;

public interface ItemService {

	TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(int page, int rows);
	TaotaoResult addItem(TbItem item, String desc);
	TbItemDesc getItemDescById(long itemId);
	List<TbItem> getItem(TbItem tbItem);
	TaotaoResult deleteItem(List<Long> ids);
	TaotaoResult updateItem(TbItem item,String desc);

}
