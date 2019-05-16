package com.gtl.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.common.utils.JsonUtils;
import com.gtl.content.service.ContentService;
import com.gtl.jedis.JedisClient;
import com.gtl.mapper.TbContentMapper;
import com.gtl.pojo.TbContent;
import com.gtl.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT}")
    private  String INDEX_CONTENT;


    @Override
    public EasyUIDataGridResult getContentList(Long categoryId,int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria=example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExample(example);

        //取查询结果
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        //返回结果
        return result;
    }

    /**
     * 添加内容
     * @param content
     * @return
     */
    @Override
    public gtlResult addContent(TbContent content) {
        //补全pojo属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入到内容表中
        tbContentMapper.insert(content);
        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT,content.getCategoryId().toString());
        return gtlResult.ok();
    }

    /**
     * 更新内容
     * @param content
     * @return
     */
    @Override
    public gtlResult updateContent(TbContent content) {
        //补全属性
        content.setUpdated(new Date());
        tbContentMapper.updateByPrimaryKeySelective(content);
        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT,content.getCategoryId().toString());
        return gtlResult.ok();
    }

    /**
     * 删除内容
     * @param list
     * @return
     */
    @Override
    public gtlResult deleteContent(List<Long> list) {
        //根据内容id获取分类id
        TbContent content=tbContentMapper.selectByPrimaryKey(list.get(0));

        //批量删除
        for (Long l:list){
            TbContentExample example=new TbContentExample();
            TbContentExample.Criteria criteria=example.createCriteria();
            criteria.andIdEqualTo(l);
            tbContentMapper.deleteByExample(example);
        }

        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT,content.getCategoryId().toString());
        return gtlResult.ok();
    }

    @Override
    public List<TbContent> getContentByCid(long cid) {
        //先查询缓存
        //添加缓存不能影响正常业务逻辑
        try{
            //查询缓存
            String json=jedisClient.hget(INDEX_CONTENT,cid+"");
            //判断是否为空
            if (StringUtils.isNotBlank(json)){
                List<TbContent> list= JsonUtils.jsonToList(json,TbContent.class);
                return list;
            }
            //查询到结果
        }catch(Exception e){
            e.printStackTrace();
        }
        //缓存中没有命中，需要查询数据库
        TbContentExample example=new TbContentExample();
        TbContentExample.Criteria criteria=example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        //执行查询
        List<TbContent> list=tbContentMapper.selectByExample(example);
        //把结果添加到缓存
        try{
            jedisClient.hset(INDEX_CONTENT,cid+"", JsonUtils.objectToJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }
        //返回结果
        return list;
    }
}
