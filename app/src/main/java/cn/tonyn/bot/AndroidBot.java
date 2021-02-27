package cn.tonyn.bot;


import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

import cn.tonyn.file.Logger;
import cn.tonyn.value.Values;
import static cn.tonyn.value.Values.bot;
public class AndroidBot {
    public static void login(long QQ,String pwd){
        Logger.l("数据路径="+Values.rootpath);
        Logger.l("当前登录账号:"+QQ+"密码:"+pwd);
        bot = BotFactory.INSTANCE.newBot(QQ, pwd, new BotConfiguration() {{
            fileBasedDeviceInfo(Values.rootpath+"data/config/devices/"+QQ+".json");
            setProtocol(MiraiProtocol.ANDROID_PAD);
            setWorkingDir(new File(Values.rootpath+"/sdcard/Coishi"));
        }});
        Values.running=true;
        bot.login();
        EventHandler.waitFor(100);
        if(bot.isOnline()){
            Logger.l("====登录成功====");
        }else{
            Logger.l("登录失败!请检查mirai日志","Error");
        }

        bot.getFriends().forEach(friend -> {
            if(!new File(Values.rootpath+"data/config/friends/"+friend.getId()+".txt").isFile()) {
                ProcessingLevel.set(friend, 0);
            }

        });

        bot.getGroups().forEach(group -> {
            if(!new File(Values.rootpath+"data/config/groups/"+group.getId()+".txt").isFile()) {
                ProcessingLevel.set(group, 0);
            }
        });

        Listener GroupMsg = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            EventHandler.GrpMsg(event);

        });

        Listener FriendMsg = GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, event->{
            EventHandler.FrdMsg(event);
        });

        Listener MemberJoin = GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinEvent.class, event ->{
            EventHandler.MemberJoin(event);
        });


        bot.join();
    }

}
