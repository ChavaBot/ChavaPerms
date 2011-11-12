package com.alta189.chavaperms;

import com.alta189.chavabot.plugins.java.JavaPlugin;

public class ChavaPerms extends JavaPlugin {
	private static PermsManager perms = null;
		
	@Override
	public void onEnable() {
		ChavaPerms.perms = new PermsManager(this.getDataFolder());
		
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
