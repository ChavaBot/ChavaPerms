package com.alta189.chavaperms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alta189.chavabot.util.SettingsHandler;

public class PermsManager {
	private SettingsHandler accounts;
	private SettingsHandler perms;
	private SettingsHandler identify;
	private SettingsHandler inherits;
	private SettingsHandler groupsSH;
	private final File permsFolder;
	private List<PermsUser> users = new ArrayList<PermsUser>();
	private List<PermsGroup> groups = new ArrayList<PermsGroup>();
	
	protected PermsManager(File permsFolder) {
		this.permsFolder = permsFolder;
	}
	
	protected void load() throws IOException {
		File accountFile = new File(permsFolder, "accounts.properties");
		File permsFile = new File(permsFolder, "perms.properties");
		File identifyFile= new File(permsFolder, "identify.properties");
		File inheritsFile = new File(permsFolder, "inherits.properties");
		File groupsFile = new File(permsFolder, "groups.properties");
		
		if (!accountFile.exists()) accountFile.createNewFile();
		if (!permsFile.exists()) permsFile.createNewFile();
		if (!identifyFile.exists()) identifyFile.createNewFile();
		if (!inheritsFile.exists()) inheritsFile.createNewFile();
		if (!groupsFile.exists()) groupsFile.createNewFile();
		
		accounts = new SettingsHandler(accountFile);
		perms = new SettingsHandler(permsFile);
		identify = new SettingsHandler(identifyFile);
		inherits = new SettingsHandler(inheritsFile);
		groupsSH = new SettingsHandler(groupsFile);
		
		accounts.setCached(true);
		perms.setCached(true);
		identify.setCached(true);
		inherits.setCached(true);
		groupsSH.setCached(true);

		accounts.load();
		perms.load();
		identify.load();
		inherits.load();
		groupsSH.load();
		
		build();
	}
	
	private void build() {
		String groupRaw = null;
		String hostname = null;
		String permsRaw = null;
		String pass = null;
		
		for (String account : accounts.getAsMap().keySet()) {
			hostname = accounts.getPropertyString(account, null);
			pass = identify.getPropertyString(account, null);
			PermsUser user = new PermsUser(account, hostname, pass);
			
			if (inherits.checkProperty(account)) {
				groupRaw = inherits.getPropertyString(account, "default");
				for (String g : groupRaw.split(",")) {
					user.addGroup(g);
				}
			}
			
			if (perms.checkProperty(account)) {
				permsRaw = perms.getPropertyString(account, "default");
				for (String p : permsRaw.split(",")) {
					user.addPerm(p);
				}
			}
			
			users.add(user);
			
			pass = null;
			hostname = null;
			groupRaw = null;
		}
		
		for (String name : groupsSH.getAsMap().keySet()) {
			PermsGroup group = new PermsGroup(name);
			permsRaw = perms.getPropertyString(name, "none");
			
			for (String perm : permsRaw.split(",")) {
				group.addPerm(perm);
			}
			groups.add(group);
		}
	}

	protected void flush() {
		accounts.reset();
		perms.reset();
		identify.reset();
		inherits.reset();
		groupsSH.reset();
		
		for (PermsUser user : users) {
			accounts.put(user.getAccount(), user.getAccountHostname());
			identify.put(user.getAccount(), user.getPass());
			
			StringBuilder gb = new StringBuilder();
			for (String group : user.getGroups()) {
				gb.append(group).append(",");
			}
			inherits.put(user.getAccount(), gb.toString());
			
			StringBuilder pb = new StringBuilder();
			for (String perm : user.getPerms()) {
				pb.append(perm).append(",");
			}
			perms.put(user.getAccount(), pb.toString());
		}
		
		for (PermsGroup group : groups) {
			StringBuilder pb = new StringBuilder();
			for (String perm : group.getPerms()) {
				pb.append(perm).append(",");
			}
			perms.put(group.getName(), pb.toString());
		}
		
	}
	
	public boolean hasPerms(String nick, String perm) {
		for (PermsUser user : users) {
			if (nick.equalsIgnoreCase(nick)) {
				if (user.isIdentifed()) {
					if (user.hasPerm(perm)) {
						return true;
					} else {
						for (String group : user.getGroups()) {
							for (PermsGroup g : groups) {
								if (g.getName().equalsIgnoreCase(group)) {
									return g.hasPerm(perm);
								}
							}
						}
					}
				}
				return false;
			}
		}
		return false;
	}
}
