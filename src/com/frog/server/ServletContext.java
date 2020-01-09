package com.frog.server;

import java.util.HashMap;
import java.util.Map;

public class ServletContext {


    private Map<String,String> servlet;
    //为每一个servlet取别名（用来组成url）  如：login->loginServlet
    private Map<String,String> mapping;
    //使每一个url对应一个servlet，一个servlet可以有多个url与之对应
    // 如：log->login，login->login，即一个servlet有多个mapping

    public ServletContext() {
        servlet = new HashMap<String,String>();
        mapping = new HashMap<String,String>();

    }

    public Map<String, String> getServlet() {
        return servlet;
    }

    public void setServlet(Map<String, String> servlet) {
        this.servlet = servlet;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }
}
