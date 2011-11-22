package com.alta189.chavaperms;

import java.io.IOException;

import com.alta189.chavabot.events.Order;
import com.alta189.chavabot.events.botevents.PrivateMessageEvent;
import com.alta189.chavabot.events.channelevents.ChannelJoinEvent;
import com.alta189.chavabot.events.channelevents.MessageEvent;
import com.alta189.chavabot.events.userevents.NickChangeEvent;
import com.alta189.chavabot.plugins.java.JavaPlugin;

public class ChavaPerms extends JavaPlugin {
	private static PermsManager perms = null;
		
	@Override
	public void onEnable() {
		ChavaPerms.perms = new PermsManager(this.getDataFolder());
		try {
			ChavaPerms.perms.load();
		} catch (IOException e) {
			e.printStackTrace();
			getPluginLoader().disablePlugin(this);
			return;
		}
		NickChangeEvent.register(new PermsNickChangeListener(), Order.Monitor, this);
		PrivateMessageEvent.register(new PermsPrivateMessageListener(), Order.Monitor, this);
		MessageEvent.register(new ChannelMessageListener(), Order.Monitor, this);
		ChannelJoinEvent.register(new ChannelJoinListener(), Order.Monitor, this);
	}

	@Override
	public void onDisable() {
		try {
			ChavaPerms.perms.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ChavaPerms.perms = null;
	}
	
	public static PermsManager getPermsManager() {
		return perms;
	}

}
