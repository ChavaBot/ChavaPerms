package com.alta189.chavaperms;

import java.io.IOException;

import com.alta189.chavabot.events.Order;
import com.alta189.chavabot.events.botevents.PrivateMessageEvent;
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
	}

	@Override
	public void onDisable() {
		ChavaPerms.perms.flush();
		ChavaPerms.perms = null;
	}
	
	public static PermsManager getPermsManager() {
		return perms;
	}

}
