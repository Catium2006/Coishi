package cn.tonyn.bot;


import java.io.File;
import java.util.Random;

import cn.tonyn.file.Logger;
import cn.tonyn.file.TextFile;
import cn.tonyn.value.Values;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.utils.ExternalResource;

import static cn.tonyn.value.Values.BatteryNow;
import static cn.tonyn.value.Values.bot;
import static cn.tonyn.value.Values.rootpath;


public class EventHandler {
	static void GrpMsg(GroupMessageEvent event) {
		String msg=event.getMessage().contentToString();
    	long FromGroup =event.getGroup().getId();
    	long Sender=event.getSender().getId();
		TextFile.Write(Values.rootpath+"data/消息记录/群/"+FromGroup+".txt",Sender+":"+msg+System.getProperty("line.separator"));
    	if(Values.debug) {
			Logger.l("收到群消息：" + FromGroup + "(" + event.getGroup().getName() + "):" + msg,"Debug");
		}

    	//白名单
    	if(!(ProcessingLevel.get(event.getGroup())==0)) {
    	    if(msg.equals("*帮助")||msg.equals("$帮助")){
                event.getGroup().sendMessage("https://github.com/TonyNomoney/Coishi/blob/main/docs/%E6%8C%87%E4%BB%A4%E5%88%97%E8%A1%A8.md");
            }
			if(msg.contains("*注册用户")||msg.equals("$注册用户")) {
				File user = new File(Values.rootpath + "data/用户/" + Sender + ".txt");
				if (!user.isFile()) {
					String 账户 = msg.replace("*注册用户", "");
					TextFile.Write(Values.rootpath + "data/用户/" + Sender + ".txt", 账户);
					TextFile.Write(Values.rootpath + "data/背包/" + Sender + ".txt", "IDCard*1,饼干*10,");
					event.getGroup().sendMessage("你有身份了:" + 账户);
					ProcessingLevel.set(Sender, 1);
				} else {
					event.getGroup().sendMessage("你已经有一个账户了");
				}
			}
			//发送者有账户
    		if(!(ProcessingLevel.get(Sender)==0)) {
    			if((msg.startsWith("$"))||(msg.startsWith("*"))) {
    				msg=msg.replace("$" , "");
    				msg=msg.replace("*" , "");
    				msg=msg.replace("$ " , "");
    				msg=msg.replace("* " , "");
    				if(msg.equals("签到")){

                    }
    				if(msg.equals("随机弔图")){
						File folder = new File(Values.rootpath+"data/图片/随机弔图");
						File []list = folder.listFiles();
						int fileCount = 0;
						long length = 0;
						for (File file : list){
							if (file.isFile()){
								fileCount++;
								length += file.length();
							}
						}
						Random r = new Random();
						int i=r.nextInt(fileCount);
						File file =new File(Values.rootpath+"data/图片/随机弔图/image"+i+".jpg");
						Image image=event.getGroup().uploadImage(ExternalResource.create(file));
						// 上传一个图片并得到 Image 类型的 Message
						event.getGroup().sendMessage(image); // 发送图片
					}
    				if(msg.equals("背包")) {
        				String contents=TextFile.Read(Values.rootpath+"data/背包/"+Sender+".txt");
        				event.getGroup().sendMessage("你的背包:\r\n"+contents);
        			}
    				if(msg.startsWith("查看")) {
    					String thing=msg.replace("查看", "");
    					File f=new File(Values.rootpath+"data/信息/"+thing+".txt");
    					if(f.isFile()) {
    						String contents=TextFile.Read(f);
    						event.getGroup().sendMessage("有关"+thing+"的信息:\r\n"+contents);
    					}else {
    						event.getGroup().sendMessage("没有相关信息");
    					}
    				}
    				if(msg.startsWith("E")) {
    					String thing=msg.replace("E", "");
    					if((TextFile.Read(Values.rootpath+"data/config/食物.txt")).contains(thing)){
    						//可以作为模板
    						if(TextFile.Read(Values.rootpath+"data/背包/"+Sender+".txt").contains(thing)) {
    							String[] CONTENTS=(TextFile.Read(Values.rootpath+"data/背包/"+Sender+".txt")).split(",");
    							for(String c:CONTENTS) {
    								if(c.contains(thing)) {
    									String all=TextFile.Read(Values.rootpath+"data/背包/"+Sender+".txt");
    									String[] cc=c.split("\\*");
    									String s=cc[0];
    									int i=Integer.parseInt(cc[1]);
    									i=i-1;
    									if(!(i==0)) {
    										String ccc=s+"*"+i;
    										all=all.replace(c, ccc);
    									}else {
    										all=all.replace(c, "");
    										all=all.replace(",,", ",");
    									}
    									TextFile.Empty(Values.rootpath+"data/背包/"+Sender+".txt");
    									TextFile.Write(Values.rootpath+"data/背包/"+Sender+".txt", all);
    									event.getGroup().sendMessage("你吃了一个"+thing+",感觉还行");
    								}
    							}
    						}else {
    							event.getGroup().sendMessage("你没有这个东西");
    						}
    					}else {
							event.getGroup().sendMessage("你真的要吃这个吗?");
						}
    					
    				}
    			
        			
        		}
    			//管理员
    			if(msg.startsWith("#")) {
    				if((ProcessingLevel.get(Sender)==20)) {
    					msg=msg.replace("#", "");
    					if(msg.equals("系统信息")){
    						long time=System.currentTimeMillis();
    						time=time-Values.starttime;
    						//得到秒数
    						long seconds=time/1000;

    						String TimeAvailable="";
    						//如果消耗电量不是0
    						if((Values.BatteryPrime-Values.BatteryNow)>0){
								long v=seconds/(Values.BatteryPrime-Values.BatteryNow);
								long TimeAvailableL=BatteryNow*v;
								TimeAvailable=TimeAvailableL+"s";
							}if((Values.BatteryPrime-Values.BatteryNow)==0){
    							TimeAvailable="[Unknown]";
							}if((Values.BatteryPrime-Values.BatteryNow)<0){
                                TimeAvailable="[充电中]";
                            }
    						String send2="操作系统信息:\r\n├操作系统:"+System.getProperty("os.name")+"/"+System.getProperty("os.arch")+"/"+System.getProperty("os.version")+"\r\n├程序运行目录:"+System.getProperty("user.dir")+"\r\n├设备用户:"+System.getProperty("user.name")+"\r\n├JVM:"+System.getProperty("java.vm.name")+"/"+System.getProperty("java.vm.version")+"\r\n├当前电量:"+Values.BatteryNow+"％\r\n应用进程信息:\r\n├运行时间:"+seconds+"s\r\n├保留进程:"+Values.keepAppRunning+"\r\n├线程数:"+Values.NumbeOfThreads+"\r\n├消耗的电量:"+(Values.BatteryPrime-Values.BatteryNow)+"％\r\n├预计可运行时间:"+TimeAvailable;
							event.getGroup().sendMessage(send2);
						}
    					if(msg.equals("BotOff")) {
    	    				event.getGroup().sendMessage("收到关闭指令,即将关闭");
    	    				bot.getFriend(148125778).sendMessage(event.getSenderName()+"执行关闭命令");
    	    				Logger.l("关闭机器人","Debug");
    	    				System.exit(0);
    	    			}
    	    			if(msg.equals("Debug")) {
    	    				Values.debug=!Values.debug;
    	        			if(Values.debug) {
    	            			event.getGroup().sendMessage("Debug模式开启");
    	            			Logger.l("Debug开启","Debug");
    	            		}else {
    	            			event.getGroup().sendMessage("Debug模式关闭");
    	            			Logger.l("Debug关闭","Debug");
    	            		}
    	    			}
    	    			if(msg.contains("set")) {

    	    				String[] SET=msg.split(" ",4);
    	    				String type=SET[1];
    	    				int level= Integer.valueOf(SET[3]).intValue();
    	    				if(type.equals("g")) {
    	    					Group group=bot.getGroupOrFail(Long.parseLong(SET[2]));
    	    					ProcessingLevel.set(group, level);
    	    					event.getGroup().sendMessage("设置成功");
    	    				}
    	    				if(type.equals("f")) {
    	    					long friendId=Long.parseLong(SET[2]);
    	    					ProcessingLevel.set(friendId, level);
    	    					event.getGroup().sendMessage("设置成功");
    	    				}

    	    			}
    	    			if(msg.equals("存活测试")){
    	    				long time=0;
    	    				while(true){
								try {
									Thread.sleep(10000);
								} catch(InterruptedException ex) {
									Thread.currentThread().interrupt();
								}
								time=time+10;
								event.getGroup().sendMessage("存活时间:"+time+"s");
							}
						}
    				}else {
        				event.getGroup().sendMessage(msg+":permission denied");
        			}
    			}
    			
    		}
    		
    		

    		if(Values.debug) {
    			event.getGroup().sendMessage("你发送了"+msg);
    		}
    	}
	}
	
	static void FrdMsg(FriendMessageEvent event) {

		String msg=event.getMessage().contentToString();
    	long Sender = event.getFriend().getId();
		TextFile.Write(Values.rootpath+"data/消息记录/好友/"+Sender+".txt",msg+System.getProperty("line.separator"));
    	if(Values.debug) {
    		Logger.l("收到好友消息:"+msg,"Debug");
    	}
    	if(!(ProcessingLevel.get(Sender)==0)) {

    		if(ProcessingLevel.get(event.getFriend())==20) {
    			if(msg.equals("BotOff")) {
    				event.getFriend().sendMessage("收到关闭命令,即将关闭");
    				Logger.l("关闭机器人","Debug");
    				System.exit(0);
    			}
    			if(msg.equals("Debug")) {
    				Values.debug=!Values.debug;
        			if(Values.debug) {
            			event.getFriend().sendMessage("Debug开启");
            			Logger.l("Debug开启","Debug");
            		}else {
            			event.getFriend().sendMessage("Debug关闭");
            			Logger.l("Debug关闭","Debug");
            		}
    			}
    			if(msg.contains("set")) {
    				String[] SET=msg.split(" ",4);
    				String type=SET[1];
    				int level= Integer.valueOf(SET[3]).intValue();
    				if(type.equals("g")) {
    					if(level==2) {
    						TextFile.Empty(Values.rootpath+"data/config/MCGroup.txt");
    						TextFile.Write(Values.rootpath+"data/config/MCGroup.txt", SET[2]);
    					}
    					Group group=bot.getGroupOrFail(Long.parseLong(SET[2]));
    					ProcessingLevel.set(group, level);
    					event.getFriend().sendMessage("设置成功");
    				}
    				if(type.equals("f")) {
    					long friendId=Long.parseLong(SET[2]);
    					ProcessingLevel.set(friendId, level);
    					event.getFriend().sendMessage("设置成功");
    				}

    			}
    			
    			
    		}
    		
    	}
    	
    	
    	
    			
    		
    	
	}
	
	static void MemberJoin(MemberJoinEvent event) {
		long FromGroup=event.getGroup().getId();
		if(ProcessingLevel.get(event.getGroup())==2) {
			event.getGroup().sendMessage("欢迎加入本群！\r\n我是本群的机器人(之一)，你可以使用*帮助获取帮助");
		}
	}
	
	
	
	
	
	static void exit() {
		Logger.l("收到关闭指令","Debug");
		try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
		System.exit(0);
	}
}
