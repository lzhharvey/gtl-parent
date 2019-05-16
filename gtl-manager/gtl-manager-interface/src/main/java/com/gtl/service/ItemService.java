package com.gtl.service;

import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.pojo.TbItem;
import com.gtl.pojo.TbItemDesc;

import java.util.List;

public interface ItemService {

	TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(int page, int rows);
	gtlResult addItem(TbItem item, String desc);
	TbItemDesc getItemDescById(long itemId);
	List<TbItem> getItem(TbItem tbItem);
	gtlResult deleteItem(List<Long> ids);
	gtlResult updateItem(TbItem item,String desc);


}
