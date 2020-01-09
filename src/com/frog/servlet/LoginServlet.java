package com.frog.servlet;

import com.frog.server.Request;
import com.frog.server.Response;

public class LoginServlet extends Servlet{
    @Override
    public void doGet(Request request, Response response) throws Exception {
        if (checkLogin(request.getParameter("uname"),request.getParameter("pwd"))){
            response.println("<html><head><title>HTTP响应示例</title>");
            response.println("</head><body>");
            response.println("login welcome:").println(request.getParameter("uname"));
            response.println("</body></html>");
        }else {
            response.println("<html><head><title>HTTP响应示例</title>");
            response.println("</head><body>");
            response.println("登录失败:").println(request.getParameter("uname"));
            response.println("</body></html>");
        }
    }
    public boolean checkLogin(String name,String psw){
        return name.equals("zhang")&& psw.equals("123");
    }
    @Override
    public void doPost(Request request, Response response) throws Exception {
    }
}
