package com.hevery.servlet;

import com.hevery.annotion.Controller;
import com.hevery.annotion.Qualifier;
import com.hevery.annotion.RequestMapping;
import com.hevery.annotion.Service;
import com.hevery.controller.MyController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 分发请求
 * @author: hongyu.zhang
 * @create: 2018-03-20
 **/
@WebServlet("/")
public class DispatcherServlet extends HttpServlet{

    private List<String> classNames = new ArrayList<String>();

    private Map<String,Object> intanceMap = new HashMap<String, Object>();

    private Map<String,Method> methodMap = new HashMap<String, Method>();
    @Override
    public void init() throws ServletException {
        /**找到bean*/
        scanBean("com.hevery");
        try {
            /**生成并注册bean*/
            filterAndInstance();
            /**注入bean*/
            springDi();
            /**处理请求*/
            mvc();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void mvc() {
        if(intanceMap.size() == 0){
            return;
        }
        for(Map.Entry<String,Object> entry : intanceMap.entrySet()){
            if(entry.getValue().getClass().isAnnotationPresent(Controller.class)){
                String controllerUrl = entry.getValue().getClass().getAnnotation(Controller.class).value();
                Method[] methods = entry.getValue().getClass().getDeclaredMethods();
                for(Method method : methods){
                    if(method.isAnnotationPresent(RequestMapping.class)){
                        String requestMappingUrl  = method.getAnnotation(RequestMapping.class).value();
                        String dispatchUrl = "/"+controllerUrl+"/"+requestMappingUrl;
                        methodMap.put(dispatchUrl,method);
                    }
                }
            }
        }
    }

    /**
     *  将instanceMap中存的bean注入到代码中对应的字段中。
     *  把每一个对象对应的Filed取出来判断一下有没有@Qualifier注解，如果有的话吧@Qulifier的value取出来，
     *  根据value从instanceMap中取出对应的实例，并赋给@Qualifier注解的Filed中
     */
    private void springDi() throws Exception{
        if(intanceMap.size() == 0){
            return;
        }
        for(Map.Entry<String,Object> entry : intanceMap.entrySet()){
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for(Field field : fields){
                if(field.isAnnotationPresent(Qualifier.class)){
                    String key = field.getAnnotation(Qualifier.class).value();
                    field.setAccessible(true);
                    //field.set(对象,新值);----->有问题
                    field.set(entry.getValue(),intanceMap.get(key));
                }
            }
        }
    }

    /**
     * 生成并注册bean(list["myServiceImpl","myController"])
     * @throws ClassNotFoundException
     */
    private void filterAndInstance() throws Exception{
        if(classNames.size() == 0){
            return;
        }
        //遍历classNames，根据类的全限定名得到对应的Class对象
        for (String className : classNames) {
            Class clazz = Class.forName(className.replace(".class",""));
            if(clazz.isAnnotationPresent(Controller.class)){
                //得到MyController的一个实例
                Object instance = clazz.newInstance();
                //得到的是Controller注解的value("hello")
                String key = ((Controller)clazz.getAnnotation(Controller.class)).value();
                // map{["hello",new MyController()]}
                intanceMap.put(key,instance);
            }else if(clazz.isAnnotationPresent(Service.class)){
                Object instance = clazz.newInstance();
                //得到的是Service注解的value("myServiceImpl")
                String key = ((Service)clazz.getAnnotation(Service.class)).value();
                // map{["myServiceImpl",new MyServiceImpl()]}
                intanceMap.put(key,instance);
            }

        }
    }

    /**
     * 扫描basePackages下所有的bean(类),并将其文件全路径加入到classNames这个List中
     * @param basePackages
     */
    private void scanBean(String basePackages) {
        URL url = this.getClass().getClassLoader().getResource("/"+replacePath(basePackages));
        //path: /com/hevery
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
        // myspring/hello/get
        String url = req.getRequestURI();
        String projectname = req.getContextPath();
        String path = url.replaceAll(projectname,"");
        Method method = methodMap.get(path);
        String className = url.split("/")[1];
        System.out.println(url.split("/").length);
        String[]ss = url.split("/");
        for (String s:ss) {
            System.out.println(s);
        }
        MyController myController = (MyController)intanceMap.get(className);
        try {
            method.invoke(myController,new Object[]{req,resp,"java"});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
