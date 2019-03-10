package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemCat;

public interface ItemCatService {

	List<EasyUITreeNode> getItemCatList(long parentId);

	List<TbItemCat> getItemCat(TbItemCat tbItemCat);

}
