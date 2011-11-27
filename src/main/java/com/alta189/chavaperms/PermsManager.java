package com.alta189.chavaperms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.alta189.chavabot.util.SettingsHandler;

public class PermsManager {
	private SettingsHandler accounts;
	private SettingsHandler perms;
	private SettingsHandler identify;
	private SettingsHandler inherits;
	private SettingsHandler groupsSH;
	private final File permsFolder;
	private Map<String, PermsUser> users = new HashMap<String, PermsUser>();
	private Map<String, PermsGroup> groups = new HashMap<String, PermsGroup>();
	private Map<String, String> nicks = new HashMap<String, String>();

	protected PermsManager(File permsFolder) {
		this.permsFolder = permsFolder;
		if (!permsFolder.exists()) permsFolder.mkdirs();
	}

	private InputStream getIS(String resource) throws IOException {
		return PermsManager.class.getResource(resource).openStream();
	}

	protected void load() throws IOException {
		File accountFile = new File(permsFolder, "accounts.properties");
		File permsFile = new File(permsFolder, "perms.properties");
		File identifyFile = new File(permsFolder, "identify.properties");
		File inheritsFile = new File(permsFolder, "inherits.properties");
		File groupsFile = new File(permsFolder, "groups.properties");

		accounts = new SettingsHandler(getIS("accounts"), accountFile);
		perms = new SettingsHandler(getIS("perms"), permsFile);
		identify = new SettingsHandler(getIS("identify"), identifyFile);
		inherits = new SettingsHandler(getIS("inherits"), inheritsFile);
		groupsSH = new SettingsHandler(getIS("perms"), groupsFile);

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
					user.addPerm(p.toLowerCase());
				}
			}

			users.put(account.toLowerCase(), user);

			pass = null;
			hostname = null;
			groupRaw = null;
		}

		for (String name : groupsSH.getAsMap().keySet()) {
			PermsGroup group = new PermsGroup(name);
			permsRaw = groupsSH.getPropertyString(name, "none");

			for (String perm : permsRaw.split(",")) {
				group.addPerm(perm);
			}
			groups.put(name.toLowerCase(), group);
		}
	}

	protected void flush() throws IOException {
		accounts.reset(getIS("accounts"));
		perms.reset(getIS("perms"));
		identify.reset(getIS("identify"));
		inherits.reset(getIS("inherits"));
		groupsSH.reset(getIS("perms"));

		for (String account : users.keySet()) {
			PermsUser user = users.get(account);
			accounts.put(user.getAccount(), user.getAccountHostname());
			identify.put(user.getAccount(), user.getPass());

			StringBuilder gb = new StringBuilder();
			if (user.getGroups() != null) {
				for (String group : user.getGroups()) {
					gb.append(group).append(",");
				}
			} else {
				gb.append("");
			}
			inherits.put(user.getAccount(), gb.toString());

			StringBuilder pb = new StringBuilder();
			if (user.getPerms() != null) {
				for (String perm : user.getPerms()) {
					pb.append(perm).append(",");
				}
			} else {
				pb.append("");
			}
			perms.put(user.getAccount(), pb.toString());
		}

		for (String g : groups.keySet()) {
			PermsGroup group = groups.get(g);
			StringBuilder pb = new StringBuilder();
			for (String perm : group.getPerms()) {
				pb.append(perm).append(",");
			}
			groupsSH.put(group.getName(), pb.toString());
		}

	}

	public boolean hasPerms(String nick, String perm) {
		PermsUser user = users.get(nick.toLowerCase());
		if (user == null) {
			String alt = nicks.get(nick);
			if (alt == null) {
				if (groups.get("default") == null)
					return false;
				return groups.get("default").hasPerm(perm);
			}
			user = users.get(alt);
			if (user == null) {
				if (groups.get("default") == null)
					return false;
				return groups.get("default").hasPerm(perm);
			}
		}
		if (user.isIdentifed()) {
			if (user.hasPerm(perm)) {
				return true;
			} else {
				for (String group : user.getGroups()) {
					PermsGroup g = groups.get(group);
					if (g != null && g.hasPerm(perm)) {
						return true;
					}
				}
			}
		}

		if (groups.get("default") == null)
			return false;
		return groups.get("default").hasPerm(perm);
	}

	public boolean hasAccount(String account) {
		PermsUser user = users.get(account);
		if (user == null) {
			String alt = nicks.get(account);
			if (alt == null) return false;
			user = users.get(alt);
			if (user == null) return false;
		}
		return (user != null);
	}
	
	public PermsUser getAccount(String account) {
		PermsUser user = users.get(account);
		if (user == null) {
			String alt = nicks.get(account);
			if (alt == null) return null;
			user = users.get(alt);
			if (user == null) return null;
		}
		return user;
	}

	public String getMappedAccount(String nick) {
		return nicks.get(nick);
	}

	public void mapAccount(String nick, String account) {
		nicks.put(nick.toLowerCase(), account);
	}

	public void removeMap(String nick) {
		nicks.remove(nick.toLowerCase());
	}

	public void addAccount(PermsUser account) {
		users.put(account.getAccount(), account);
	}

	public boolean identify(String account, String pass) {
		PermsUser user = users.get(account);
		if (user == null) {
			String alt = nicks.get(account);
			if (alt == null) return false;
			user = users.get(alt);
			if (user == null) return false;
			return user.identify(pass);		
		}
		if (user.identify(pass)) {
			mapAccount(account, account);
		}
		
		return false;
	}

	public boolean hostIdentify(String account, String nick, String hostname) {
		PermsUser user = users.get(account);
		if (user == null) {
			String alt = nicks.get(account);
			if (alt == null) return false;
			user = users.get(alt);
			if (user == null) return false;
			user.setHostname(hostname);
			user.setNick(nick);
			return user.isIdentifed();
		}
		user.setHostname(hostname);
		user.setNick(nick);
		if (user.isIdentifed()) {
			mapAccount(account, nick);
		}
		return false;
	}
	
	public boolean isValidGroup(String group) {
		return groups.containsKey(group.toLowerCase());
	}
	
	public void addGroup(PermsGroup g) {
		groups.put(g.getName().toLowerCase(), g);
	}
	
	public PermsGroup getGroup(String group) {
		return groups.get(group.toLowerCase());
	}
}
