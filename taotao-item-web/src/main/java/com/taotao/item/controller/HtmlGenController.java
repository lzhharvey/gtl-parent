package com.taotao.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 网页静态化处理
 */
@Controller
public class HtmlGenController {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 访问时要加.html后缀
     * @return
     */
    @RequestMapping("/genHtml")
    @ResponseBody
    public String genHtml(){
        try {
            //生成静态页面
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("hello.ftl");
            Map data=new HashMap();
            data.put("hello","spring freemarker test");
            Writer out=new FileWriter(new File("D:\\ee28\\out\\hello1.html"));
            template.process(data,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

}
