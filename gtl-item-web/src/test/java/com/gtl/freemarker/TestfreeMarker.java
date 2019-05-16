package com.gtl.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class TestfreeMarker {
    @Test
    public void testFreeMarker() throws  Exception{
        //创建一个模板文件
        //创建一个Configuration对象
        Configuration configuration=new Configuration(Configuration.getVersion());
        //设置模板所在的路径
        configuration.setDirectoryForTemplateLoading(new File("F:\\MyProject\\JavaSSM\\day03\\source\\gtl-parent\\gtl-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
        //设置模板的字符集 utf-8
        configuration.setDefaultEncoding("utf-8");
        //使用Cinfiguration加载模板文件，指定模板文件的文件名
        Template template = configuration.getTemplate("student.ftl");
        //创建一个数据集，可以是pojo也可以是map  推荐map
        Map data=new HashMap();
        data.put("hello","hello freemarker");
        Student student=new Student(1,"哈哈",123,"阿斯达克");
        data.put("student",student);
        List<Student> studentList=new ArrayList<>();
        studentList.add(new Student(1,"笑眯眯1",12,"asd"));
        studentList.add(new Student(2,"笑眯眯2",13,"asd"));
        studentList.add(new Student(3,"笑眯眯3",14,"asd"));
        studentList.add(new Student(4,"笑眯眯4",15,"asd"));
        studentList.add(new Student(5,"笑眯眯5",16,"asd"));
        studentList.add(new Student(6,"笑眯眯6",17,"asd"));
        studentList.add(new Student(7,"笑眯眯7",18,"asd"));
        data.put("stuList",studentList);
        //日期类型的处理
        data.put("date",new Date());
        data.put("val","AShjh");
        //创建一个writer对象，指定输出文件的路径和文件名
        Writer out =new FileWriter("D:\\ee28\\out\\student.html");
        //使用模板对象的process方法输出文件
        template.process(data,out);
        //关闭流
        out.close();
    }

}
