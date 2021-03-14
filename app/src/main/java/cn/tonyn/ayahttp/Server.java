package cn.tonyn.ayahttp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import cn.tonyn.file.Logger;
import cn.tonyn.value.Values;


public class Server {
    String BLANK=" ";
    String LN="\n";
    int Port;
    public Server(int port) {
        Port=port;
    }
    public void Listen() {
        new Thread(){
            @Override
            public void run(){
                try {
                    ServerSocket server = new ServerSocket(Port);
                    while(true) {
                        Socket client = server.accept();
                        StringBuilder responseContext1 =new StringBuilder();
                        responseContext1.append("<html><head>在线日志</head><body><h1>Coishi日志</h1><h2>Powered by AyaHttp 1.0 (Unoffical Edition)</h2>");
                        responseContext1.append(Values.AllLog.toString());
                        responseContext1.append("</body></html>");
                        StringBuilder response1 =new StringBuilder();
                        response1.append("HTTP/1.1").append(BLANK).append("200").append(BLANK).append("OK").append(LN);
                        response1.append("Server:AyaHttp Server/1.0.0").append(LN);
                        response1.append("Date:").append(new Date()).append(LN);
                        response1.append("Content-type:text/html;charset=UTF-8").append(LN);
                        response1.append("Content-Length:").append(responseContext1.toString().getBytes().length).append(LN);
                        response1.append(LN);
                        response1.append(responseContext1);
                        BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                        bw1.write(response1.toString());
                        bw1.flush();
                        client.getOutputStream().flush();
                        System.out.println(responseContext1);

                        Logger.l("收到http请求","AyaHttp");



                    }
                } catch (IOException e) {
                    Logger.l(e.getMessage(),"IOException");
                }
            }
        }.start();
    }
    void WaitFor(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e){
            Logger.l(e.getMessage(),"Exception");
        }
    }
    public int getPort(){
        return Port;
    }
}
