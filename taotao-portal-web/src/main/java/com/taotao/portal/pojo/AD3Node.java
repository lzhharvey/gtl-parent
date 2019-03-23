package com.taotao.portal.pojo;

import com.taotao.pojo.TbItem;

import java.util.List;

public class AD3Node {
    private String title;
    private List<TbItem> tbItems;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TbItem> getTbItems() {
        return tbItems;
    }

    public void setTbItems(List<TbItem> tbItems) {
        this.tbItems = tbItems;
    }
}
