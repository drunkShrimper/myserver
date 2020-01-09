package com.frog.server;

import com.frog.utils.CloseUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Response {

    public static final String CRLF="\r\n";
    public static final String BLANK=" ";
    private int len;

    private StringBuilder headInfo;
    private StringBuilder content;

    private BufferedWriter bw;

    public Response() {
        headInfo = new StringBuilder();
        content = new StringBuilder();
        len = 0;
    }
    public Response(Socket client) {
        this();
        try {
            bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            headInfo = null;
        }
    }
    public Response(OutputStream os) {
        this();
        bw = new BufferedWriter(new OutputStreamWriter(os));
    }

    public Response print(String info){
        content.append(info);
        len += info.getBytes().length;
        return this;
    }

    public Response println(String info){
        content.append(info).append(CRLF);
        len += (info+CRLF).getBytes().length;
        return this;
    }

    private void createHeadInfo(int code){
        //1)  HTTP协议版本、状态代码、描述
        headInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
        switch (code){
            case 200:
                headInfo.append("OK");
                break;
            case 404:
                headInfo.append("NOT FOUND");
                break;
            case 500:
                headInfo.append("SERVER ERROR");
                break;
        }

        headInfo.append(CRLF);
        //2)  响应头(Response Head)
        headInfo.append("Server:frog Server/0.0.1").append(CRLF);
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Content-type:text/html;charset=UTF-8").append(CRLF);
        //正文长度 ：字节长度
        headInfo.append("Content-Length:").append(len).append(CRLF);
        //3)正文之前
        headInfo.append(CRLF);
    }

    public void pushToClient(int code){

        createHeadInfo(code);
        if(null == headInfo){
            code = 500;
        }

        try {
            bw.append(headInfo.toString());
            bw.append(content.toString());
            bw.flush();
        } catch (IOException e) {
            pushToClient(500);
        }
    }
    public void close(){
        CloseUtil.closeIO(bw);
    }
}
