package cn.tonyn.bot;


import java.io.File;

import cn.tonyn.file.Logger;
import cn.tonyn.file.TextFile;
import cn.tonyn.value.Values;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;

public class ProcessingLevel {
	/**
	 * 输入群组和级别，进行设置
	 * @param group
	 * @param level
	 */
	static public void set(Group group,int level) {
		long groupl=group.getId();
		TextFile.Empty(Values.rootpath+"data/config/groups/"+groupl+".txt");
		TextFile.Write(Values.rootpath+"data/config/groups/"+groupl+".txt", ""+level);
		Logger.l("设置了群"+groupl+"为"+level);
	}

	/**
	 * 输入好友和级别进行设置
	 * @param friend
	 * @param level
	 */
    static public void set(Friend friend,int level) {
    	long friendl=friend.getId();
		TextFile.Empty(Values.rootpath+"data/config/friends/"+friendl+".txt");
		TextFile.Write(Values.rootpath+"data/config/friends/"+friendl+".txt", ""+level);
		Logger.l("设置了好友"+friendl+"为"+level);
	}
    static public void set(long friendId,int level) {
		TextFile.Empty(Values.rootpath+"data/config/friends/"+friendId+".txt");
		TextFile.Write(Values.rootpath+"data/config/friends/"+friendId+".txt", ""+level);
		Logger.l("设置了好友"+friendId+"为"+level);
	}

	/**
	 * 获取群的级别
	 * @param group
	 * @return 返回级别
	 */
	static public int get(Group group) {
		long groupl=group.getId();
		File f=new File(Values.rootpath+"data/config/groups/"+groupl+".txt");
		if(!f.isFile()) {
			set(group,0);
		}
		int i = Integer.valueOf(TextFile.Read(Values.rootpath+"data/config/groups/"+groupl+".txt")).intValue();
		return i;
	}
	static public int get(long FriendId) {
		File f=new File(Values.rootpath+"data/config/friends/"+FriendId+".txt");
    	if(!f.isFile()) {
    		set(FriendId,0);
    	}
		int i = Integer.valueOf(TextFile.Read(Values.rootpath+"data/config/friends/"+FriendId+".txt")).intValue();
		return i;
	}
	static public int get(Friend friend) {
		long friendl=friend.getId();
		File f=new File(Values.rootpath+"data/config/friends/"+friendl+".txt");
		if(!f.isFile()) {
    		set(friendl,0);
    	}
		int i = Integer.valueOf(TextFile.Read(Values.rootpath+"data/config/friends/"+friendl+".txt")).intValue();
		return i;
	}
}
