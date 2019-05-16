package com.gtl.search.mapper;

import com.gtl.common.pojo.SearchItem;

import java.util.List;

public interface SearchItemMapper {
    List<SearchItem> getItemList();
    SearchItem getItemById(long itemId);
}
