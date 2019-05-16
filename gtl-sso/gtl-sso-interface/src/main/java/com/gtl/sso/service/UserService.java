package com.gtl.sso.service;

import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.pojo.TbItem;
import com.gtl.pojo.TbUser;

import java.util.List;

public interface UserService {
    /**
     * 1.1.1.检查数据是否可用
     * @param data
     * @param type
     * @return
     */
    gtlResult checkData(String data,int type);

    /**
     * 1.1.2.用户注册
     * @param tbUser
     * @return
     */
    gtlResult register(TbUser tbUser);

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    gtlResult login(String username,String password);

    /**
     * 根据token查询用户
     * @param token
     * @return
     */
    gtlResult getUserByToken(String token);

    /**
     * 安全退出
     */
    gtlResult logout(String token);

    /**
     * 获取所有用户
     */
    EasyUIDataGridResult getUserList(int page, int rows);

    /**
     * 删除用户
     * @param ids
     * @return
     */
     gtlResult deleteUser(List<Long> ids);

    /**
     * 更新用户
     * @param user
     * @return
     */
    gtlResult updateUser(TbUser user);


}
