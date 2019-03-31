package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

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
		System.out.println("Aa");
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		System.out.println("bb");
		return result;
	}
	
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult addItem(TbItem item, String desc) {
		TaotaoResult result = itemService.addItem(item, desc);
		return result;
	}

	@RequestMapping(value = "/item/delete")
	@ResponseBody
	public TaotaoResult deleteItem(@RequestParam  List<Long> ids){
		try {
			 itemService.deleteItem(ids);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}

	@RequestMapping(value = "/item/update")
	@ResponseBody
	public TaotaoResult updateItem( TbItem tbitem,String desc){
		try {
			itemService.updateItem(tbitem,desc);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}


}
