package com.taotao.sso.service.impl;
import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.Jedis;

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
    public TaotaoResult checkData(String data, int type) {
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
            return  TaotaoResult.build(400,"请求参数包含非法参数");
        }
        List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);

        if(tbUserExample!=null && tbUsers.size()>0){
            //查询到数据，返回false
            return TaotaoResult.ok(false);
        }
        //数据可以使用
        return TaotaoResult.ok(true);
    }

    /**
     * 用户注册
     * @param tbUser
     * @return
     */
    @Override
    public TaotaoResult register(TbUser tbUser) {
        //检查数据的有效性
        if(StringUtils.isBlank(tbUser.getUsername())){
            return TaotaoResult.build(400,"用户名不能为空");
        }
        //判断用户名是否重复
        TaotaoResult taotaoResult = checkData(tbUser.getUsername(), 1);
        if(!(boolean)taotaoResult.getData()){
            return TaotaoResult.build(400,"用户名重复");
        }
        //判断密码是否为空
        if(StringUtils.isBlank(tbUser.getPassword())){
            return TaotaoResult.build(400,"密码不能为空");
        }
        if (StringUtils.isNotBlank(tbUser.getPhone())){
            //是否重复的电话号码
            taotaoResult=checkData(tbUser.getPhone(),2);
            if (!(boolean)taotaoResult.getData()){
                return TaotaoResult.build(400,"手机号重复");
            }
        }
        if (StringUtils.isNotBlank(tbUser.getEmail())){
            //是否重复的邮箱
            taotaoResult=checkData(tbUser.getEmail(),3);
            if (!(boolean)taotaoResult.getData()){
                return TaotaoResult.build(400,"邮箱重复");
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
        return TaotaoResult.ok();
    }

    /**
     * 登陆
     */
    @Override
    public TaotaoResult login(String username, String password) {
        //判断用户名和密码是否正确
        TbUserExample example=new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if(tbUsers==null || tbUsers.size()==0){
            //返回登陆失败
            return TaotaoResult.build(400,"用户名或密码不正确");
        }
        TbUser tbUser=tbUsers.get(0);
        //密码进行md5加密  然后校验
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())){
            //返回登陆失败
            return TaotaoResult.build(400,"用户名或密码不正确");
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
        return TaotaoResult.ok(token);
    }
    /**
     * 根据token查询用户
     * @param token
     * @return
     */
    @Override
    public TaotaoResult getUserByToken(String token) {
        String s = jedisClient.get(USER_SESSION+":"+token);

        if (StringUtils.isBlank(s)){
            return TaotaoResult.build(400,"用户登录已经过期");
        }
        //重置session的过期时间
        jedisClient.expire(USER_SESSION+":"+token,SESSION_EXPIRE);
        TbUser tbUser = JSON.parseObject(s, TbUser.class);

        return TaotaoResult.ok(tbUser);
    }

    /**
     * 安全退出
     * @param token
     * @return
     */
    @Override
    public TaotaoResult logout(String token) {
        Long del = jedisClient.del(USER_SESSION + ":" + token);

        if(del==0){
            return TaotaoResult.build(400,"用户登录已经过期");
        }
        return TaotaoResult.ok();
    }
}
