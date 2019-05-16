package com.gtl.controller;

import com.gtl.common.pojo.EasyUIDataGridResult;
import com.gtl.common.pojo.gtlResult;
import com.gtl.pojo.TbUser;
import com.gtl.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/user/list")
    @ResponseBody
    public EasyUIDataGridResult getUserList(Integer page,Integer rows){
        EasyUIDataGridResult userList = userService.getUserList(page, rows);
        return userList;
    }


    @RequestMapping("/user/update")
    @ResponseBody
    public gtlResult  updateUser(TbUser tbUser){
        System.out.println(tbUser.getId()+" "+tbUser.getUsername()+"  "+tbUser.getEmail());
        gtlResult gtl = userService.updateUser(tbUser);
        return  gtl.ok();
    }

    @RequestMapping("/user/delete")
    @ResponseBody
    public  gtlResult deleteUser(@RequestParam List<Long> ids){
        gtlResult gtl = userService.deleteUser(ids);
        return gtl.ok();
    }



}
