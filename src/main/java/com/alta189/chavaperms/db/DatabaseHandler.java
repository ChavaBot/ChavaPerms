package com.alta189.chavaperms.db;

import java.util.ArrayList;
import java.util.List;

import com.alta189.chavabot.plugin.CommonPlugin;
import com.alta189.chavabot.util.Database;
import com.alta189.chavaperms.db.data.Account;
import com.alta189.chavaperms.db.data.Group;
import com.alta189.chavaperms.db.data.WhitelistedAccount;
import com.avaje.ebean.EbeanServer;

public class DatabaseHandler extends Database {
	private EbeanServer ebean;
	
	public DatabaseHandler(CommonPlugin plugin) {
		super(plugin);
	}
	
	public void setup() {
		initializeDatabase("org.sqlite.JDBC", "jdbc:sqlite:{DIR}{NAME}.db", "perms", "supersecure", "SERIALIZABLE", true, false);
		ebean = getDatabase();
	}

	protected List<Class<?>> getDatabaseClasses() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(Account.class);
		list.add(Group.class);
		list.add(WhitelistedAccount.class);
		return list;
	}

	public EbeanServer getDatabase() {
		return ebean;
	}
	
	private Account getAccountNoWhitelist(String name) {
		return ebean.find(Account.class).where().ieq("account", name).findUnique();
	}
	
	public WhitelistedAccount getWhitelistedAccount(String name) {
		return ebean.find(WhitelistedAccount.class).where().ieq("name", name).findUnique();
	}
	
	public Account getAccount(String name) {
		name = name.toLowerCase();
		Account result = getAccountNoWhitelist(name);
		if (result == null) {
			WhitelistedAccount account = getWhitelistedAccount(name);
			if (account != null) {
				result = ebean.find(Account.class).where().ieq("account", account.getAccount()).findUnique();
			}
		}
		return result;
	}
	
	public boolean identify(String nick, String hostname) {
		nick = nick.toLowerCase();
		hostname = hostname.toLowerCase();
		Account account = getAccountNoWhitelist(nick);
		if (account != null) {
			if (account.getHostname().equals(hostname)) {
				account.setIdentified(true);
			} else {
				account.setIdentified(false);
			}
			ebean.save(account);
		}
		WhitelistedAccount wa = getWhitelistedAccount(nick);
		if (wa != null) {
			account = getAccountNoWhitelist(wa.getAccount());
			if (account != null) {
				if (wa.getHostname().equals(hostname)) {
					account.setIdentified(true);
				} else {
					account.setIdentified(false);
				}
				ebean.save(account);
			}
		}
		return false;
	}
	
	public void saveAccount(Account account) {
		ebean.save(account);
	}
	
	public void remAccount(Account account) {
		ebean.delete(account);
	}
	
	public boolean hasAccount(String name) {
		return getAccount(name) != null;
	}
	
	public void saveWhitelistedAccount(WhitelistedAccount account) {
		ebean.save(account);
	}
	
	public void remWhitelistedAccount(WhitelistedAccount account) {
		ebean.delete(account);
	}
	
	public Group getGroup(String name) {
		name = name.toLowerCase();
		return ebean.find(Group.class).where().ieq("group", name).findUnique();
	}
	
	public boolean validGroup(String name) {
		return getGroup(name) != null;
	}
	
	public void addGroup(Group group) {
		ebean.save(group);
	}
	
	public void remGroup(Group group) {
		ebean.delete(group);
	}
}
