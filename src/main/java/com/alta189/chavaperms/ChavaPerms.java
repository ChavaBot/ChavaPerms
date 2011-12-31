package com.alta189.chavaperms;

import com.alta189.chavabot.ChavaManager;
import com.alta189.chavabot.plugin.CommonPlugin;

public class ChavaPerms extends CommonPlugin {
	private static ChavaPerms instance = null;
	private PermsManager perms = null;
		
	@Override
	public void onEnable() {
		ChavaPerms.instance = this;
		perms = new PermsManager();
		perms.setup();
		ChavaManager.getListenerManager().addListener(new PermListener());
	}

	@Override
	public void onDisable() {
		perms.disable();
		perms = null;
	}
	
	public static ChavaPerms getInstance() {
		return instance;
	}
	
	public static PermsManager getPermsManager() {
		return instance.perms;
	}

}
