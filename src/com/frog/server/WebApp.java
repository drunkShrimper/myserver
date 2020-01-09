package com.frog.server;

import com.frog.servlet.Servlet;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WebApp {



    private static ServletContext servletContext;
    static {

        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();

            WebHandler handler = new WebHandler();
            saxParser.parse(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("WEB-INFO/web.xml"),handler
            );//TODO：换一个文件打开方式

            servletContext = new ServletContext();


            Map<String,String> servlet = servletContext.getServlet();
            for (Entity entity:handler.getEntityList()){
                servlet.put(entity.getName(),entity.getClz());
            }


            Map<String,String> mapping = servletContext.getMapping();
            for (Mapping mapp:handler.getMappingList()){
                List<String> urlPattern = mapp.getUrlPattern();
                for(String url:urlPattern){
                    mapping.put(url,mapp.getName());
                }
            }


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }



    }
    public static Servlet getServlet(String url){
        if(null == url || url.trim().equals("")){
            return null;
        }
        String name = servletContext.getServlet().get(servletContext.getMapping().get(url));
        try {
            return (Servlet) Class.forName(name).newInstance();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InstantiationException e) {
            return null;
        }
    }
}
