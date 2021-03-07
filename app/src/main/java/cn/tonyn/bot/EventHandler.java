package cn.tonyn.bot;


import java.io.File;
import java.util.Random;

import cn.tonyn.file.Logger;
import cn.tonyn.file.TextFile;
import cn.tonyn.util.Download;
import cn.tonyn.value.Values;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import static cn.tonyn.value.Values.BatteryNow;
import static cn.tonyn.value.Values.bot;
import static cn.tonyn.value.Values.rootpath;


public class EventHandler {
	//复读
	static String msg0="";
	static String msg1="";
	static String msg2="";

	//群消息处理
	static void GrpMsg(GroupMessageEvent event) {
		String msg=event.getMessage().contentToString();
    	long FromGroup =event.getGroup().getId();
    	long Sender=event.getSender().getId();
    	//记录所有消息
		Logger.l(event.getGroup()+"-"+Sender+":"+msg,"msg");
		TextFile.Write(Values.rootpath+"data/消息记录/群/"+FromGroup+".txt",Sender+":"+msg+System.getProperty("line.separator"));
    	if(Values.debug) {
			Logger.l("收到群消息：" + FromGroup + "(" + event.getGroup().getName() + "):" + msg,"Debug");
		}

    	//群白名单
    	if(!(ProcessingLevel.get(event.getGroup())==0)) {
    		//等待100ms，防止出现消息看不到的问题
    		waitFor(100);
    		//复读
			msg0=msg1;
    		msg1=msg2;
    		msg2=msg;
    		if(msg1.equals(msg2)){
				event.getGroup().sendMessage(msg);
			}
    		//帮助
    	    if(msg.equals("*帮助")||msg.equals("$帮助")){
    	    	//发送markdown的链接
                event.getGroup().sendMessage("https://github.com/TonyNomoney/Coishi/blob/main/docs/%E6%8C%87%E4%BB%A4%E5%88%97%E8%A1%A8.md");
            }
    	    //注册用户
			if(msg.contains("*注册用户")||msg.equals("$注册用户")) {
				File user = new File(Values.rootpath + "data/用户/" + Sender + ".txt");
				if (!user.isFile()) {
					String 账户 = msg.replace("*注册用户", "");
					TextFile.Write(Values.rootpath + "data/用户/" + Sender + ".txt", 账户);
					TextFile.Write(Values.rootpath + "data/背包/" + Sender + ".txt", "IDCard*1,饼干*10,");
					event.getGroup().sendMessage("你有身份了:" + 账户);
					ProcessingLevel.set(Sender, 1);
				} else {
					//如果已经有此用户
					event.getGroup().sendMessage("你已经有一个账户了");
				}
			}
			//如果发送者有账户
    		if(!(ProcessingLevel.get(Sender)==0)) {
    			//如果发送的是指令
    			if((msg.startsWith("$"))||(msg.startsWith("*"))) {
    				//获取有效信息
    				msg=msg.replace("$" , "");
    				msg=msg.replace("*" , "");
    				msg=msg.replace("$ " , "");
    				msg=msg.replace("* " , "");
    				//随机图片
    				if(msg.equals("随机图片")){
    					//先得到图库总数
						File folder = new File(Values.rootpath+"data/图片/随机图片");
						File []list = folder.listFiles();
						int fileCount = 0;
						long length = 0;
						for (File file : list){
							if (file.isFile()){
								fileCount++;
								length += file.length();
							}
						}
						//得到一张随机图片
						Random r = new Random();
						int i=r.nextInt(fileCount);
						File file =new File(Values.rootpath+"data/图片/随机图片/image"+i+".jpg");
						// 上传一个图片并得到 Image 类型的 Message
						Image image=event.getGroup().uploadImage(ExternalResource.create(file));
						event.getGroup().sendMessage(image); // 发送图片
					}
    				if(msg.startsWith("图库添加")){
						//获取图片数
						File folder = new File(Values.rootpath+"data/图片/随机图片");
						File []list = folder.listFiles();
						int fileCount = 0;
						long length = 0;
						for (File file : list){
							if (file.isFile()){
								fileCount++;
								length += file.length();
							}
						}
						int i=fileCount;
						String FilePath= rootpath+"data/图片/随机图片/image"+i+".jpg";
						//经测试下载到的图片可能会出现“未经允许不得使用”的问题
						Download.DownloadImageFromMsg(event.getMessage(),FilePath);
						event.getGroup().sendMessage("保存成功");

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
    				/*if(msg.equals("背包")) {
        				String contents=TextFile.Read(Values.rootpath+"data/背包/"+Sender+".txt");
        				event.getGroup().sendMessage("你的背包:\r\n"+contents);
        			}*/

    				/*if(msg.startsWith("E")) {
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
    					
    				}*/
    			
        			
        		}
    			//管理员
    			if(msg.startsWith("#")) {
    				if((ProcessingLevel.get(Sender)==20)) {
    					msg=msg.replace("#", "");
    					if(msg.equals("日志")){
    						//请改为您机器人设备的ip并进行端口转发
    						event.getGroup().sendMessage("查看全部日志:http://www.tonyn.cn:10020");
						}
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
    						String send="操作系统信息:\r\n├操作系统:"+System.getProperty("os.name")+"/"+System.getProperty("os.arch")+"/"+System.getProperty("os.version")+"\r\n├程序运行目录:"+System.getProperty("user.dir")+"\r\n├设备用户:"+System.getProperty("user.name")+"\r\n├JVM:"+System.getProperty("java.vm.name")+"/"+System.getProperty("java.vm.version")+"\r\n├当前电量:"+Values.BatteryNow+"％\r\n应用进程信息:\r\n├运行时间:"+seconds+"s\r\n├保留进程:"+Values.keepAppRunning+"\r\n├线程数:"+Values.NumbeOfThreads+"\r\n├消耗的电量:"+(Values.BatteryPrime-Values.BatteryNow)+"％\r\n├预计可运行时间:"+TimeAvailable;
							event.getGroup().sendMessage(send);
						}

    					if(msg.equals("BotOff")) {
    						//关闭
    	    				event.getGroup().sendMessage("收到关闭指令,即将关闭");
    	    				Logger.l("关闭机器人","Debug");
    	    				exit();
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
    	    				//设置处理级别
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
    	    				//测试机器人存活时间
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
    					//没得权限
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
    	Logger.l(event.getFriend()+":"+msg,"msg");
		TextFile.Write(Values.rootpath+"data/消息记录/好友/"+Sender+".txt",msg+System.getProperty("line.separator"));
    	if(Values.debug) {
    		Logger.l("收到好友消息:"+msg,"Debug");
    	}
    	if(!(ProcessingLevel.get(Sender)==0)) {

    		if(ProcessingLevel.get(event.getFriend())==20) {
				if(msg.contains("[图片]")){
					//获取图片数
					File folder = new File(Values.rootpath+"data/图片/随机图片");
					File []list = folder.listFiles();
					int fileCount = 0;
					long length = 0;
					for (File file : list){
						if (file.isFile()){
							fileCount++;
							length += file.length();
						}
					}
					int i=fileCount;
					String FilePath= rootpath+"data/图片/随机图片/image"+i+".jpg";
					Download.DownloadImageFromMsg(event.getMessage(),FilePath);
					event.getFriend().sendMessage("保存成功");

				}
    			if(msg.equals("BotOff")) {
    				event.getFriend().sendMessage("收到关闭命令,即将关闭");
    				Logger.l("关闭机器人","Debug");
    				exit();
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

	}

	
	
	public static void waitFor(int somewhile){
		try {
			Thread.sleep(somewhile);
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	static void exit() {
		Logger.l("收到关闭指令","Debug");
		waitFor(1000);
		System.exit(0);
	}
}
