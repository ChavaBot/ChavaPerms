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
}
