package cn.tonyn.coishi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

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
        new File(Values.rootpath+"data/msg").mkdirs();
        new File(Values.rootpath+"data/背包").mkdirs();
        new File(Values.rootpath+"data/信息").mkdirs();
        new File(Values.rootpath+"data/账户").mkdirs();
        TextFile.Write(Values.rootpath+"data/config/食物.txt","饼干,");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getPermissions();
        mkDirs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.启动).setOnClickListener(this::onClick);


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

    public void onClick(View view){
        int id=view.getId();
        if(id==R.id.启动){
            Logger.l("启动按钮点击");
            Values.running=true;
            AndroidBot.run();
        }
    }
}
