package com.gtl.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.pojo.TbItem;
import com.gtl.service.ItemService;

import java.util.List;

/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	public gtlResult addItem(TbItem item, String desc) {
		gtlResult result = itemService.addItem(item, desc);
		return result;
	}

	@RequestMapping(value = "/item/delete")
	@ResponseBody
	public gtlResult deleteItem(@RequestParam  List<Long> ids){
		try {
			 itemService.deleteItem(ids);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return gtlResult.ok();
	}

	@RequestMapping(value = "/item/update")
	@ResponseBody
	public gtlResult updateItem( TbItem tbitem,String desc){
		try {
			itemService.updateItem(tbitem,desc);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return gtlResult.ok();
	}

	@RequestMapping(value = "/item/getItem")
	@ResponseBody
	public EasyUIDataGridResult getItem(TbItem tbItem){
			List<TbItem> item= itemService.getItem(tbItem);

			//取查询结果
			PageInfo<TbItem> pageInfo = new PageInfo<>(item);
			EasyUIDataGridResult result = new EasyUIDataGridResult();
			result.setRows(item);
			result.setTotal(pageInfo.getTotal());


			return result;

	}


}
