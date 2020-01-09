package com.frog.server;

import com.frog.servlet.Servlet;
import com.frog.utils.CloseUtil;

import java.io.IOException;
import java.net.Socket;

public class Dispatcher implements Runnable{

    private Socket client;
    private Request request;
    private Response response;

    private int code = 200;

    public Dispatcher() {
    }

    public Dispatcher(Socket client) {
        this.client = client;
        try {
            request = new Request(this.client.getInputStream());
            response = new Response(this.client.getOutputStream());
        } catch (IOException e) {
            code = 500;
        }

    }

    @Override
    public void run() {


        try {
            Servlet servlet = WebApp.getServlet(request.getUrl());
            if (null == servlet){
                this.code = 404;
            }else{
                servlet.service(request,response);
            }
            response.pushToClient(code);
        } catch (Exception e) {
            this.code = 500;
        }


        CloseUtil.closeSocket(client);
    }
}
