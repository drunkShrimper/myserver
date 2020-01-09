package com.frog.server;

import javax.lang.model.element.NestingKind;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class Request {

    public static final String CRLF="\r\n";

    private String method;
    private String url;
    private Map<String, List<String>> parameterMapValues;

    private InputStream is;
    private String requestInfo;


    public Request() {
        method = null;
        parameterMapValues = new HashMap<String, List<String>>();
        requestInfo = "";
    }

    public Request(InputStream is) {
        this();
        this.is = is;
        byte[] data = new byte[20480];
        int len = 0;
        try {
            len = is.read(data);
        } catch (IOException e) {
            return;
        }
        requestInfo = new String(data,0,len+1-1).trim();
        //这个加一减一很神奇，如果直接写len会报指针溢出错...但是这样用Chrome访问不存在的url也会报这个错误
        //StringIndexOutOfBoundsException: String index out of range: -1
        parseRequestInfo();
    }
    private  String decode(String value,String code){
        try {
            return URLDecoder.decode(value,code);
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
    private void parseRequestInfo(){
        if(null == requestInfo || (requestInfo = requestInfo.trim()).equals("")){
            return;
        }
        String paramString = "";
        String firstLine = requestInfo.substring(0,requestInfo.indexOf(CRLF));
        int idx = firstLine.indexOf("/");   //第一个/的位置
        this.method = firstLine.substring(0,idx).trim();
        String urlStr = firstLine.substring(idx,firstLine.indexOf("HTTP/")).trim();
        if(this.method.equalsIgnoreCase("post")){
            this.url = urlStr;
            paramString = requestInfo.substring(requestInfo.lastIndexOf(CRLF)).trim();
        }else if(this.method.equalsIgnoreCase("get")){
            if(urlStr.contains("?")){
                String[] urlArr = urlStr.split("\\?");
                this.url = urlArr[0];
                paramString = urlArr[1];
            }else{
                this.url = urlStr;
            }
        }
        if (!paramString.equals("")){
            this.parseParam(paramString);
        }

    }

    private void parseParam(String paramString){

        StringTokenizer tokenizer = new StringTokenizer(paramString,"&");
        while (tokenizer.hasMoreTokens()){
            String keyValue = tokenizer.nextToken();
            String[] keyValueArr = keyValue.split("=");
            if(keyValueArr.length == 1){
                keyValueArr = Arrays.copyOf(keyValueArr,2);
                keyValueArr[1] = null;
            }
            String key = keyValueArr[0].trim();
            String value = null==keyValueArr[1]?null:decode(keyValueArr[1].trim(),"GBK");
            if(!parameterMapValues.containsKey(key)){
                parameterMapValues.put(key,new ArrayList<String>());
            }
            List<String> values = parameterMapValues.get(key);
            values.add(value);
        }



    }

    public String[] getParameterValues(String name){
        List<String> values = null;
        if (null == (values = parameterMapValues.get(name))){
            return null;
        }else{
            return values.toArray(new String[0]);
        }
    }

    public String getParameter(String name){
        String[] values = getParameterValues(name);
        if(null == values){
            return null;
        }else{
            return values[0];
        }
    }


    public String getUrl() {
        return url;
    }
}
