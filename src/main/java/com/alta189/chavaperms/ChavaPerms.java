package com.alta189.chavaperms;

import java.io.File;
import java.io.IOException;

import com.alta189.chavabot.events.Order;
import com.alta189.chavabot.events.botevents.PrivateMessageEvent;
import com.alta189.chavabot.events.channelevents.ChannelJoinEvent;
import com.alta189.chavabot.events.channelevents.MessageEvent;
import com.alta189.chavabot.events.userevents.NickChangeEvent;
import com.alta189.chavabot.plugins.java.JavaPlugin;

public class ChavaPerms extends JavaPlugin {
	private static ChavaPerms instance = null;
	private PermsManager perms = null;
		
	@Override
	public void onEnable() {
		ChavaPerms.instance = this;
		perms = new PermsManager(this.getDataFolder());
		try {
			perms.load();
		} catch (IOException e) {
			e.printStackTrace();
			getPluginLoader().disablePlugin(this);
			return;
		}
		NickChangeEvent.register(new PermsNickChangeListener(), Order.Earlist, this);
		PrivateMessageEvent.register(new PermsPrivateMessageListener(), Order.Default, this);
		MessageEvent.register(new ChannelMessageListener(), Order.Earlist, this);
		ChannelJoinEvent.register(new ChannelJoinListener(), Order.Earlist, this);
	}

	@Override
	public void onDisable() {
		try {
			perms.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		perms = null;
	}
	
	public static PermsManager getPermsManager() {
		return instance.perms;
	}
	
	public static File getFolder() {
		return instance.getDataFolder();
	}

}
