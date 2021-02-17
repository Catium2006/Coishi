package cn.tonyn.util;


import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

public class Download{
    public static InputStream getInputStreamFromUrl(String ImageUrl) {
        InputStream inputStream = null;
        try{
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(ImageUrl).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36");
            httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
            httpURLConnection.setRequestProperty("Referer","no-referrer");
            httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(20000);
            inputStream = httpURLConnection.getInputStream();
        }catch (IOException e){
            e.printStackTrace();
        }
        return inputStream;
    }



    public static boolean StreamToFile(InputStream inputStream, String path){
        boolean flag = true;
        File file = new File(path);
        if (file.exists()){
            return flag;
        }
        File fileParent = file.getParentFile();
        if (!fileParent.exists()){
            fileParent.mkdirs();//创建路径
        }
        try {
            OutputStream out=new FileOutputStream(path);
            int temp = 0;
            // 开始拷贝
            while ((temp = inputStream.read()) != -1) {
                // 边读边写
                out.write(temp);
            }
        }catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static void DownloadImageFromMsg(MessageChain chain,String filepath) {
        //这部分是空白写的https://github.com/kongbai-s
        // TODO Auto-generated method stub
        System.out.println("chain码->" + chain);
        Image image = chain.get(Image.Key);
        String ImageId=StrUtil.removeAll(StrUtil.removeAll(image.getImageId(), "}"), "{");
        System.out.println(image);
        String fileUrl = Image.queryUrl(image);
        System.out.println(Image.queryUrl(image));
        System.out.println(ImageId);
        // 带进度显示的文件下载
        HttpUtil.downloadFile(fileUrl, FileUtil.file(filepath), new StreamProgress() {

            @Override
            public void start() {
                Console.log("开始下载。。。。");
            }

            @Override
            public void progress(long progressSize) {
                Console.log("已下载：{}", FileUtil.readableFileSize(progressSize));
            }

            @Override
            public void finish() {
                Console.log("下载完成！");
            }
        });
    }

}