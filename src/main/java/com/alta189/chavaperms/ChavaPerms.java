package com.alta189.chavaperms;

import java.io.File;

import com.alta189.chavabot.events.Order;
import com.alta189.chavabot.events.botevents.PrivateMessageEvent;
import com.alta189.chavabot.events.channelevents.ChannelJoinEvent;
import com.alta189.chavabot.events.channelevents.MessageEvent;
import com.alta189.chavabot.plugins.java.JavaPlugin;

public class ChavaPerms extends JavaPlugin {
	private static ChavaPerms instance = null;
	private PermsManager perms = null;
		
	@Override
	public void onEnable() {
		ChavaPerms.instance = this;
		perms = new PermsManager();
		perms.setup();
		PrivateMessageEvent.register(new PermsPrivateMessageListener(), Order.Default, this);
		MessageEvent.register(new ChannelMessageListener(), Order.Earlist, this);
		ChannelJoinEvent.register(new ChannelJoinListener(), Order.Earlist, this);
	}

	@Override
	public void onDisable() {
		perms = null;
	}
	
	public static PermsManager getPermsManager() {
		return instance.perms;
	}
	
	public static File getFolder() {
		return instance.getDataFolder();
	}

}
