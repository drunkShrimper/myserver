package com.frog.servlet;

import com.frog.server.Request;
import com.frog.server.Response;

public class RegisterServlet extends Servlet{
    @Override
    public void doGet(Request request, Response response) throws Exception {
        response.println("<html><head><title>HTTP响应示例</title>");
        response.println("</head><body>");
        response.println("register welcome:").println(request.getParameter("uname"));
        response.println("</body></html>");
    }

    @Override
    public void doPost(Request request, Response response) throws Exception {
    }
}
