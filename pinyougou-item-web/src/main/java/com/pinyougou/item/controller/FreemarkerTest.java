package com.pinyougou.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreemarkerTest {
    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration(Configuration.VERSION_2_3_26);
        config.setDefaultEncoding("UTF-8");
        //config.setClassLoaderForTemplateLoading(FreemarkerTest01.class.getClassLoader(), "/ftl");
        config.setClassForTemplateLoading(FreemarkerTest.class, "/ftl");
        Template template = config.getTemplate("hello.ftl");

        Map<String, Object> modelData = new HashMap<>();
        modelData.put("username", "余浩宏");
        modelData.put("msg", "欢迎使用free marker");
        modelData.put("age", 14);

        List<Map<String, Object>> users = new ArrayList<>();
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "余浩宏1");
        user1.put("age", 30);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "余浩宏2");
        user2.put("age", 25);

        Map<String, Object> user3 = new HashMap<>();
        user3.put("name", "余浩宏3");
        user3.put("age", 20);
        users.add(user1);
        users.add(user2);
        users.add(user3);

        modelData.put("users", users);


        FileWriter fw = new FileWriter("C:/Users/82545/Desktop/hello.html");
        template.process(modelData, fw);
    }
}
