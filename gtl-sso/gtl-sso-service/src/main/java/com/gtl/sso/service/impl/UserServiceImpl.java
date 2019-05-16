package com.gtl.sso.service.impl;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.jedis.JedisClient;
import com.gtl.mapper.TbUserMapper;
import com.gtl.pojo.*;
import com.gtl.pojo.TbUser;
import com.gtl.pojo.TbUserExample;
import com.gtl.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户处理Service
 */
@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${USER_SESSION}")
    private String USER_SESSION;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    /**
     * 1.1.1.检查数据是否可用
     * @param data
     * @param type
     * @return
     */
    @Override
    public gtlResult checkData(String data, int type) {
        TbUserExample tbUserExample=new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        //设置查询条件
        // 1 判断用户名是否可用
        if(type==1){
            criteria.andUsernameEqualTo(data);
            // 2 判断手机号是否可用
        }else if (type==2){
            criteria.andPhoneEqualTo(data);
            // 3 判断邮箱是否可以使用
        }else if (type==3){
            criteria.andEmailEqualTo(data);
        }else{
            return  gtlResult.build(400,"请求参数包含非法参数");
        }
        List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);

        if(tbUserExample!=null && tbUsers.size()>0){
            //查询到数据，返回false
            return gtlResult.ok(false);
        }
        //数据可以使用
        return gtlResult.ok(true);
    }

    /**
     * 用户注册
     * @param tbUser
     * @return
     */
    @Override
    public gtlResult register(TbUser tbUser) {
        //检查数据的有效性
        if(StringUtils.isBlank(tbUser.getUsername())){
            return gtlResult.build(400,"用户名不能为空");
        }
        //判断用户名是否重复
        gtlResult gtlResult = checkData(tbUser.getUsername(), 1);
        if(!(boolean)gtlResult.getData()){
            return gtlResult.build(400,"用户名重复");
        }
        //判断密码是否为空
        if(StringUtils.isBlank(tbUser.getPassword())){
            return gtlResult.build(400,"密码不能为空");
        }
        if (StringUtils.isNotBlank(tbUser.getPhone())){
            //是否重复的电话号码
            gtlResult=checkData(tbUser.getPhone(),2);
            if (!(boolean)gtlResult.getData()){
                return gtlResult.build(400,"手机号重复");
            }
        }
        if (StringUtils.isNotBlank(tbUser.getEmail())){
            //是否重复的邮箱
            gtlResult=checkData(tbUser.getEmail(),3);
            if (!(boolean)gtlResult.getData()){
                return gtlResult.build(400,"邮箱重复");
            }
        }
        //补全pojo属性
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        //密码md5加密
        String password=DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(password);
        //插入数据
        tbUserMapper.insert(tbUser);
        //注册成功
        return gtlResult.ok();
    }

    /**
     * 登陆
     */
    @Override
    public gtlResult login(String username, String password) {
        //判断用户名和密码是否正确
        TbUserExample example=new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if(tbUsers==null || tbUsers.size()==0){
            //返回登陆失败
            return gtlResult.build(400,"用户名或密码不正确");
        }
        TbUser tbUser=tbUsers.get(0);
        //密码进行md5加密  然后校验
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())){
            //返回登陆失败
            return gtlResult.build(400,"用户名或密码不正确");
        }
        //生成token,使用uuid
        String token = UUID.randomUUID().toString();
        //把用户信息保存到redis,key就是token,value就是用户信息
        //清空密码 提高安全性
        tbUser.setPassword(null);
        jedisClient.set(USER_SESSION+":"+token, JSON.toJSONString(tbUser));
        //设置key的过期时间
        jedisClient.expire(USER_SESSION+":"+token,SESSION_EXPIRE);
        //返回登陆成功，其中要把token返回
        return gtlResult.ok(token);
    }
    /**
     * 根据token查询用户
     * @param token
     * @return
     */
    @Override
    public gtlResult getUserByToken(String token) {
        String s = jedisClient.get(USER_SESSION+":"+token);

        if (StringUtils.isBlank(s)){
            return gtlResult.build(400,"用户登录已经过期");
        }
        //重置session的过期时间
        jedisClient.expire(USER_SESSION+":"+token,SESSION_EXPIRE);
        TbUser tbUser = JSON.parseObject(s, TbUser.class);

        return gtlResult.ok(tbUser);
    }

    /**
     * 安全退出
     * @param token
     * @return
     */
    @Override
    public gtlResult logout(String token) {
        Long del = jedisClient.del(USER_SESSION + ":" + token);

        if(del==0){
            return gtlResult.build(400,"用户登录已经过期");
        }
        return gtlResult.ok();
    }

    @Override
    public EasyUIDataGridResult getUserList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbUserExample example = new TbUserExample();
        List<TbUser> list = tbUserMapper.selectByExample(example);
        //取查询结果
        PageInfo<TbUser> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        //返回结果
        return result;
    }

    @Override
    public gtlResult deleteUser(List<Long> ids) {
        for (final Long l: ids){
            TbUserExample tbUserExample=new TbUserExample();
            //设置查询条件
            TbUserExample.Criteria criteria = tbUserExample.createCriteria();
            criteria.andIdEqualTo(l);
            //执行删除
            try{
                tbUserMapper.deleteByExample(tbUserExample);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return gtlResult.ok();
    }

    @Override
    public gtlResult updateUser(TbUser user) {
        //设置更新条件
        try {
            //密码md5加密
            String password=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(password);
            // 1、根据用户id，更新用户表，条件更新
            TbUserExample userExample = new TbUserExample();
            TbUserExample.Criteria criteria = userExample.createCriteria();
            criteria.andIdEqualTo(user.getId());
            tbUserMapper.updateByExampleSelective(user, userExample);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return gtlResult.ok();
    }
}
