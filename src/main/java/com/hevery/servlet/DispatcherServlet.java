package com.hevery.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 分发请求
 * @author: hongyu.zhang
 * @create: 2018-03-20
 **/
@WebServlet("/")
public class DispatcherServlet extends HttpServlet{

    private List<String> classNames = new ArrayList<String>();



    @Override
    public void init() throws ServletException {
        /**找到bean*/
        scanBean("com.hevery");
        /**生成并注册bean*/
        try {
            filterAndInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /**注入bean*/

    }

    private void filterAndInstance() throws ClassNotFoundException{
        if(classNames.size() == 0){
            return;
        }
        for (String className : classNames) {
            Class clazz = Class.forName(className.replace(".class",""));
        }
    }

    private void scanBean(String basePackages) {
        URL url = this.getClass().getClassLoader().getResource("/"+replacePath(basePackages));
        String path = url.getFile();
        File file = new File(path);
        // file.list()得到的是String类型的文件名，file.listFiles()得到的是File类型的全路径
        String[] strFiles = file.list();
        for(String strFile : strFiles){
            File eachFile = new File(path + strFile);
            if(eachFile.isDirectory()){
                scanBean(basePackages+"."+eachFile.getName());
            }else {
                System.out.println("class name" + eachFile.getName());
                classNames.add(basePackages + "." + eachFile.getName());
            }
        }

    }

    private String replacePath(String path){
        return path.replace(".","/");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
