package cn.tonyn.coishi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import cn.tonyn.bot.AndroidBot;
import cn.tonyn.file.Logger;
import cn.tonyn.file.TextFile;
import cn.tonyn.value.Values;


public class MainActivity extends AppCompatActivity {
    void getPermissions(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                //写
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                //读
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
                //网
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET},1);
            }
        }
    }

    void mkDirs(){
        new File(Values.rootpath+"data/log").mkdirs();
        new File(Values.rootpath+"data/config/devices").mkdirs();
        new File(Values.rootpath+"data/config/friends").mkdirs();
        new File(Values.rootpath+"data/config/groups").mkdirs();
        new File(Values.rootpath+"data/log").mkdirs();
        new File(Values.rootpath+"data/消息记录/群").mkdirs();
        new File(Values.rootpath+"data/消息记录/好友").mkdirs();
        new File(Values.rootpath+"data/背包").mkdirs();
        new File(Values.rootpath+"data/信息").mkdirs();
        new File(Values.rootpath+"data/用户").mkdirs();
        if(!new File(Values.rootpath+"data/config/食物.txt").isFile()){
            TextFile.Write(Values.rootpath+"data/config/食物.txt","饼干,");
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //权限和目录
        getPermissions();
        mkDirs();
        //记录时间
        Values.starttime=System.currentTimeMillis();
        //初始电量
        BatteryManager batteryManager = (BatteryManager)getSystemService(BATTERY_SERVICE);
        int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Values.BatteryPrime=battery;
        //循环线程
        loop();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.登录).setOnClickListener(this::onClick);
        findViewById(R.id.强制保留后台).setOnClickListener(this::onClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void loop(){
        new Thread(){
            @Override
            public void run(){
                Values.NumbeOfThreads++;
                while(true){
                    //获取电量
                    BatteryManager batteryManager = (BatteryManager)getSystemService(BATTERY_SERVICE);
                    int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    Values.BatteryNow=battery;
                }
            }
        }.start();
    }
    public void onClick(View view){
        int id=view.getId();
        if(id==R.id.登录){
            new Thread(){
                @Override
                public void run(){
                    Values.NumbeOfThreads++;
                    AndroidBot.run();
                }
            }.start();

            Snackbar.make(findViewById(R.id.LinearLayout), "登录", Snackbar.LENGTH_LONG).show();
            Logger.l("登录按钮点击");
        }
        if(id==R.id.强制保留后台){
            Values.keepAppRunning=!Values.keepAppRunning;
            if(Values.keepAppRunning){
                setKeepRunning();
                Snackbar.make(findViewById(R.id.LinearLayout), "开启", Snackbar.LENGTH_LONG).show();
            }else{
                stopKeepingRunning();
                Snackbar.make(findViewById(R.id.LinearLayout), "关闭", Snackbar.LENGTH_LONG).show();
            }
        }
    }
    MediaPlayer mp3player=null;
    void setKeepRunning(){
        mp3player=MediaPlayer.create(this,R.raw.music2);
        mp3player.setLooping(true);
        new Thread(){
            @Override
            public void run(){
                Values.NumbeOfThreads++;
                mp3player.start();
            }
        }.start();
    }
    void stopKeepingRunning(){
        mp3player.stop();
    }

}
