package cn.tonyn.file;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cn.tonyn.value.Values;


public class Logger{
    public static void l(String event){
        //日志
        /**
         * @auther TonyNomoney
         *
         */
        File log = new File(Values.rootpath+"data/log/Info.log");
        if(Values.debug) {//debug
            event="==Debug=="+event;
            log=new File(Values.rootpath+"data/log/Debug.log");
        }else{
            event="[Info] "+event;
        }
        //时间
        Date date = new Date();
        SimpleDateFormat bjSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     // 北京
        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区
        //输出
        String s=bjSdf.format(date)+event+"\r\n";
        Values.AllLog.append("<p>"+s+"</p>");
        Values.LOGSTRING=Values.LOGSTRING+s;
        int length=Values.LOGSTRING.length();
        if(length>512){
            Values.LOGSTRING=Values.LOGSTRING.substring(length-512, length);
        }
        System.out.println(bjSdf.format(date)+event);
        //指定了文件名
        FileWriter writer = null;
        try {
            //写入
            writer = new FileWriter(log , true);
            BufferedWriter out = new BufferedWriter(writer);
            out.write(bjSdf.format(date)+"/"+event+System.getProperty("line.separator"));
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("Error:"+e.getMessage());
            e.printStackTrace();
        }
    }
    public static void l(String event,String label){
        //类似上一个方法
        event="["+label+"] "+event;
        File log = new File(Values.rootpath+"data/log/"+label+".log");
        if(Values.debug) {//debug
            event="==Debug=="+event;
            log=new File(Values.rootpath+"data/log/Debug.log");
        }
        Date date = new Date();
        SimpleDateFormat bjSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     // 北京
        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区
        //输出
        String s=bjSdf.format(date)+event+"\r\n";
        Values.AllLog.append("<p>"+s+"</p>");
        Values.LOGSTRING=Values.LOGSTRING+s;
        int length=Values.LOGSTRING.length();
        if(length>512){
            Values.LOGSTRING=Values.LOGSTRING.substring(length-512, length);
        }
        System.out.println(bjSdf.format(date)+event);
        //指定了文件名
        FileWriter writer = null;
        try {
            writer = new FileWriter(log , true);
            BufferedWriter out = new BufferedWriter(writer);
            out.write(bjSdf.format(date)+"/"+event+System.getProperty("line.separator"));
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("Error:"+e.getMessage());
            e.printStackTrace();
        }
    }

}
