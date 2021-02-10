package cn.tonyn.file;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cn.tonyn.coishi.MainActivity;
import cn.tonyn.coishi.R;
import cn.tonyn.value.Values;


public class Logger{
    public static void l(String event){
        event="[Info] "+event;
        File log = new File(Values.rootpath+"data/log/Info.log");
        if(Values.debug) {//debug
            event="==Debug=="+event;
            log=new File(Values.rootpath+"data/log/Debug.log");
        }
        Date date = new Date();
        SimpleDateFormat bjSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     // 北京
        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区
        //输出
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
    public static void l(String event,String label){
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
