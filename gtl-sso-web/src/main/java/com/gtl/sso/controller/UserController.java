package com.gtl.sso.controller;

import com.gtl.common.pojo.gtlResult;
import com.gtl.common.utils.JsonUtils;
import com.gtl.pojo.TbUser;
import com.gtl.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户处理
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public gtlResult checkUserData(@PathVariable String param,@PathVariable Integer type){
        gtlResult gtlResult = userService.checkData(param, type);
        return gtlResult;
    }

    @RequestMapping(value ="/user/register",method = RequestMethod.POST)
    @ResponseBody
    public gtlResult  register(TbUser tbUser){
        gtlResult result = userService.register(tbUser);
        return result;
    }

    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public gtlResult login(String username, String password, HttpServletResponse response,HttpServletRequest request){
        gtlResult result = userService.login(username, password);
        //登陆成功后写入cookie
        //将token写入cookie  密码输的不对 login.getData()会空指针  原因result里的data为null
        if(result.getStatus()==200) {
            //把token写入cookie
//            System.out.println("+++++++++++++domainName: " +CookieUtils.getDomainName(request));
//            CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
            Cookie cookie = new Cookie(TOKEN_KEY, result.getData().toString());
            cookie.setDomain("localhost");
            cookie.setPath("/");
            cookie.setMaxAge(36000);
            response.addCookie(cookie);
        }
        return result;
    }

    @RequestMapping(value = "/user/token/{token}",method=RequestMethod.GET,
    //指定返回响应数据的content-type
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback){
        gtlResult result = userService.getUserByToken(token);
        //判断是否为jsonp请求
        if(StringUtils.isNotBlank(callback)){
            return callback+"("+ JsonUtils.objectToJson(result)+");";
        }
        return JsonUtils.objectToJson(result);
    }

    @RequestMapping(value = "/user/logout/{token}",method = RequestMethod.GET)
    @ResponseBody
    public Object logout(@PathVariable String token,String callback){
        gtlResult logout = userService.logout(token);
        if(StringUtils.isBlank(callback)){
            return "login";
        }else{
            MappingJacksonValue mappingJackValue = new MappingJacksonValue(logout);
            mappingJackValue.setJsonpFunction(callback);
            return mappingJackValue;
        }
    }
}
