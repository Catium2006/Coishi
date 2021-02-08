package cn.tonyn.bot;

import java.io.File;

import cn.tonyn.file.Logger;
import cn.tonyn.file.TextFile;
import cn.tonyn.value.Values;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;

public class EventHandler {
	static Bot MyBot;
	public EventHandler(Bot bot) {
		MyBot=bot;
	}
	void GrpMsg(GroupMessageEvent event) {
		String msg=event.getMessage().contentToString();
    	long FromGroup =event.getGroup().getId();
    	long Sender=event.getSender().getId();
    	if(Values.debug) {
    		Logger.l("收到群消息："+FromGroup+"("+event.getGroup().getName()+"):"+msg);
    	}
    	
    	if(msg.contains("*注册账户")) {
    		if(!(new File(Values.rootpath+"data/账户/"+Sender+".txt")).isFile()) {
    			String 账户=msg.replace("*注册账户", "");
        		TextFile.Write(Values.rootpath+Values.rootpath+"data/账户/"+Sender+".txt", 账户);
        		TextFile.Write(Values.rootpath+"data/账户/"+Sender+".txt", "IDCard*1,饼干*10,");
        		event.getGroup().sendMessage("你有身份了:"+账户);
        		ProcessingLevel.set(Sender, 1);
    		}else {
    			event.getGroup().sendMessage("你已经有一个账户了");
    		}
    	}
    	//白名单
    	if(!(ProcessingLevel.get(event.getGroup())==0)) {
    		//服务器群
			if(ProcessingLevel.get(event.getGroup())==2) {

    		}
			//发送者有账户
    		if(!(ProcessingLevel.get(Sender)==0)) {
    			if((msg.startsWith("$"))||(msg.startsWith("*"))) {
    				msg=msg.replace("$" , "");
    				msg=msg.replace("*" , "");
    				msg=msg.replace("$ " , "");
    				msg=msg.replace("* " , "");
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
    					if(msg.equals("BotOff")) {
    	    				event.getGroup().sendMessage("收到关闭指令,即将关闭");
    	    				MyBot.getFriend(148125778).sendMessage(event.getSenderName()+"执行关闭命令");
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
    	    					Group group=MyBot.getGroupOrFail(Long.parseLong(SET[2]));
    	    					if(level==2) {
    	    						TextFile.Empty(Values.rootpath+"data/config/MCGroup.txt");
    	    						TextFile.Write(Values.rootpath+"data/config/MCGroup.txt", SET[2]);
    	    					}
    	    					ProcessingLevel.set(group, level);
    	    					event.getGroup().sendMessage("设置成功");
    	    				}
    	    				if(type.equals("f")) {
    	    					long friendId=Long.parseLong(SET[2]);
    	    					ProcessingLevel.set(friendId, level);
    	    					event.getGroup().sendMessage("设置成功");
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
	
	void FrdMsg(FriendMessageEvent event) {
		String msg=event.getMessage().contentToString();
    	long Sender = event.getFriend().getId();
    	if(Values.debug) {
    		Logger.l("收到好友消息:"+msg);
    	}
    	//�ж��Ƿ��ڰ�������
    	if(!(ProcessingLevel.get(Sender)==0)) {
    		
    		//�������ǹ���Ա
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
    					Group group=MyBot.getGroupOrFail(Long.parseLong(SET[2]));
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
	
	void MemberJoin(MemberJoinEvent event) {
		long FromGroup=event.getGroup().getId();
		if(ProcessingLevel.get(event.getGroup())>0) {
			
		}
	}
	
	
	
	
	
	static void exit() {
		Logger.l("�յ��ر�ָ��,ִ���˳�����","Debug");
		//BotMain.FrdWL.Update();
		//BotMain.GrpWL.Update();
		//BotMain.AdminWL.Update();
		try {
            Thread.sleep(1000); //1000 ���룬Ҳ����1��.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
		System.exit(0);
	}
}
