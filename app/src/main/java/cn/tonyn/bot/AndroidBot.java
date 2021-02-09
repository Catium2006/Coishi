package cn.tonyn.bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.BotOfflineEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

import cn.tonyn.file.Logger;
import cn.tonyn.file.TextFile;
import cn.tonyn.util.sysinfo;
import cn.tonyn.value.Values;
import static cn.tonyn.value.Values.bot;
public class AndroidBot {
    public static void run(){

        //new Thread() {
            //@Override
            //public void run() {
        //sysinfo.main();
                long QQnum=Long.valueOf(TextFile.Read(Values.rootpath+"QQnum.txt")).longValue();
                String pwds= TextFile.Read(Values.rootpath+"pwd.txt");
                Logger.l("当前登录账号:"+QQnum+"密码:"+pwds);
                bot = BotFactory.INSTANCE.newBot(QQnum, pwds, new BotConfiguration() {{
                    setProtocol(MiraiProtocol.ANDROID_PAD);
                    fileBasedDeviceInfo(Values.rootpath+"data/config/devices/"+QQnum+".json");
                }});
                Values.running=true;
                bot.login();
                Logger.l("====登录成功====");

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

                Listener BotOfflineEvt = GlobalEventChannel.INSTANCE.subscribeAlways(BotOfflineEvent.class, event ->{
                    Logger.l(event.getBot()+"机器人已离线", "Error");
                });

                bot.join();
            //}
        //}.start();

    }

}
