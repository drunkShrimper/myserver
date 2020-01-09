package com.frog.server;

import com.frog.utils.CloseUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final String CRLF="\r\n";
    public static final String BLANK=" ";

    private ServerSocket server;
    private boolean isShutDown = false;

    public static void main(String[] args) {

        Server server = new Server();
        server.start();
    }

    public void start(){
        start(8888);
    }

    public void start(int port){
        try {
            server = new ServerSocket(port);
            this.receive();
        } catch (IOException e) {
            stop();
        }

    }

    private void receive(){
        try {
            while (!isShutDown){

                Socket client = server.accept();
                new Thread(new Dispatcher(client)).start();
            }

        }catch (IOException e){
            stop();
        }
    }

    public void stop(){
        isShutDown = true;
        CloseUtil.closeSocket(server);
    }
}
