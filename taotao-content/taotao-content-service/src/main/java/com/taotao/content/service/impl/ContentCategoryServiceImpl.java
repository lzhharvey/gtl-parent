package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        //根据parentId查询子节点列表
        TbContentCategoryExample example =new TbContentCategoryExample();
        //设置查询条件
        TbContentCategoryExample.Criteria criteria= example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> list=tbContentCategoryMapper.selectByExample(example);
        List<EasyUITreeNode> resultList=new ArrayList<>();

        for (TbContentCategory tbContentCategory :list){
            EasyUITreeNode node=new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            //添加到结果列表
            resultList.add(node);
        }

        return resultList;
    }

    @Override
    public TaotaoResult addContentCategory(Long parentId, String name) {
        //创建一个pojo对象
        TbContentCategory contentCategory=new TbContentCategory();

        //补全对象的属性
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);

        //状态，可选  1正常  2删除
        contentCategory.setStatus(1);

        //排序  默认为1
        contentCategory.setSortOrder(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());

        //插入到数据库
        tbContentCategoryMapper.insert(contentCategory);

        //判断父节点状态
        TbContentCategory parent=tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()){
            parent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果
        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public void updateContentCategory(Long id, String name) {
        //封装属性
        TbContentCategory tbContentCategory=new TbContentCategory();
        tbContentCategory.setId(id);
        tbContentCategory.setName(name);

        tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);

    }

    @Override
    public void deleteContentCategory( Long id) {
        TbContentCategory contentCategory=tbContentCategoryMapper.selectByPrimaryKey(id);

        //是文件夹，里面的都要删 ,,,如果文件夹里的文件删除完
        // 该文件夹的isparent状态改为false,文件夹才能变成文件，才能删除
        if(contentCategory.getIsParent()){
            //删除文件夹
            contentCategory.setIsParent(false);
            tbContentCategoryMapper.deleteByPrimaryKey(contentCategory.getId());
            //查询当前节点的子节点
            TbContentCategoryExample tbContentCategoryExample=new TbContentCategoryExample();
            TbContentCategoryExample.Criteria  criteria=tbContentCategoryExample.createCriteria();
            criteria.andParentIdEqualTo(id);
            //执行查询
            List<TbContentCategory> list=tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
            for (TbContentCategory tbContentCategory:list){
                deleteContentCategory(tbContentCategory.getId());
            }
        }else{ //是文件，直接删,并且如果删了该文件，同级目录没有叶子节点了，父节点isparent状态要改为false

            //删除当前文件
            tbContentCategoryMapper.deleteByPrimaryKey(contentCategory.getId());
            //获取父节点id
            Long parentid=contentCategory.getParentId();
            //查询父节点下是否还有其他节点
            TbContentCategoryExample example=new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria=example.createCriteria();
            criteria.andParentIdEqualTo(parentid);
            //执行查询
            List<TbContentCategory> list=tbContentCategoryMapper.selectByExample(example);
            if(list.size()==0){//表示父节点下已经没有子节点了，改状态
                //获取父节点
                TbContentCategory contentCategory1=tbContentCategoryMapper.selectByPrimaryKey(parentid);
                contentCategory1.setIsParent(false);
                tbContentCategoryMapper.updateByPrimaryKey(contentCategory1);
            }
        }
    }

}
