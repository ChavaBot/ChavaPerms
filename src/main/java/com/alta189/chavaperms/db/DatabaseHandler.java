package com.alta189.chavaperms.db;

import java.util.ArrayList;
import java.util.List;

import com.alta189.chavaperms.ChavaPerms;
import com.alta189.chavaperms.db.data.Account;
import com.alta189.chavaperms.db.data.Group;
import com.alta189.chavaperms.db.data.WhitelistedAccount;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.SQLitePlatform;
import com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation;

public class DatabaseHandler {
	private EbeanServer ebean;

	public void setup() {
		ServerConfig db = new ServerConfig();
		db.setDefaultServer(false);
		db.setRegister(false);
		db.setClasses(getDataClasses());
		db.setName("ChavaPerms");

		DataSourceConfig ds = new DataSourceConfig();
		ds.setDriver("org.sqlite.JDBC");
		ds.setUrl(replaceDatabaseString("jdbc:sqlite:{DIR}ChavaPerms.db"));
		ds.setUsername("ChavaPerms");
		ds.setPassword("winning");
		ds.setIsolationLevel(TransactionIsolation.getLevel("SERIALIZABLE"));

		db.setDatabasePlatform(new SQLitePlatform());
		db.getDatabasePlatform().getDbDdlSyntax().setIdentity("");

		ebean = EbeanServerFactory.create(db);
	}

	private String replaceDatabaseString(String input) {
		input = input.replaceAll("\\{DIR\\}", ChavaPerms.getFolder().getPath().replaceAll("\\\\", "/") + "/");
		return input;
	}

	private List<Class<?>> getDataClasses() {
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
