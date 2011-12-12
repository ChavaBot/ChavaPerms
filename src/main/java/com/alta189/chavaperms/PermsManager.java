package com.alta189.chavaperms;

import com.alta189.chavaperms.db.DatabaseHandler;
import com.alta189.chavaperms.db.data.Account;
import com.alta189.chavaperms.db.data.Group;
import com.alta189.chavaperms.db.data.WhitelistedAccount;

public class PermsManager {
	DatabaseHandler handler = new DatabaseHandler();
	
	public void setup() {
		ChavaPerms.getFolder().mkdirs();
		handler.setup();
	}
	
	public Account getAccount(String name) {
		return handler.getAccount(name);
	}
	
	public Group getGroup(String name) {
		return handler.getGroup(name);
	}
	
	public boolean hasPerms(String name, String perm) { 
		Account account = getAccount(name);
		if (account == null) return defaultPerm(perm);
		if (account.hasPerm(perm)) {
			return true;
		} else {
			for (String group : account.getGroups()) {
				Group g = getGroup(group);
				if (g == null) continue;
				if (g.hasPerm(perm)) {
					return true;
				}
			}
		}
		return defaultPerm(perm);
	}
	
	private boolean defaultPerm(String perm) {
		Group group = getGroup("default");
		if (group != null) {
			return group.hasPerm(perm);
		}
		return false;
	}
	
	public boolean identify(String nick, String hostname) {
		return handler.identify(nick, hostname);
	}
	
	public boolean hasAccount(String name) {
		return handler.hasAccount(name);
	}
	
	public void addAccount(Account account) {
		handler.saveAccount(account);
	}
	
	public boolean validGroup(String name) {
		return handler.validGroup(name);
	}
	
	public void addGroup(Group group) {
		handler.addGroup(group);
	}
	
	public WhitelistedAccount getWhitelistedAccount(String name) {
		return handler.getWhitelistedAccount(name);
	}
	
	public void saveWhitelistedAccount(WhitelistedAccount account) {
		handler.saveWhitelistedAccount(account);
	}
}
