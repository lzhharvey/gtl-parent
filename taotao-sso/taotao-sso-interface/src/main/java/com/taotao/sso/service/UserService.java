package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
    /**
     * 1.1.1.检查数据是否可用
     * @param data
     * @param type
     * @return
     */
    TaotaoResult checkData(String data,int type);

    /**
     * 1.1.2.用户注册
     * @param tbUser
     * @return
     */
    TaotaoResult register(TbUser tbUser);

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    TaotaoResult login(String username,String password);

    /**
     * 根据token查询用户
     * @param token
     * @return
     */
    TaotaoResult getUserByToken(String token);

    /**
     * 安全退出
     */
    TaotaoResult logout(String token);

}
