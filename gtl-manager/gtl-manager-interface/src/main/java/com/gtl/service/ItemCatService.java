package com.gtl.service;

import java.util.List;

import com.gtl.common.pojo.EasyUITreeNode;
import com.gtl.pojo.TbItemCat;

public interface ItemCatService {

	List<EasyUITreeNode> getItemCatList(long parentId);

	List<TbItemCat> getItemCat(TbItemCat tbItemCat);

}
