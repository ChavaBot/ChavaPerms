package com.alta189.chavaperms;

import java.util.ArrayList;
import java.util.List;

public class PermsUser {
	private final String account;
	private String nick;
	private final String accountHostname;
	private String pass;
	private String hostname;
	private boolean identified = false;
	private List<String> perms = new ArrayList<String>();
	private List<String> groups = new ArrayList<String>();
	
	public PermsUser(String account, String accountHostname, String pass) {
		this.account = account;
		this.accountHostname = accountHostname;
		this.pass = pass;
	}
	
	public PermsUser(String account, String accountHostname, String pass, List<String> perms) {
		this.account = account;
		this.accountHostname = accountHostname;
		this.pass = pass;
		this.perms.addAll(perms);
	}
	
	public PermsUser(String account, String accountHostname, String pass, List<String> perms, List<String> groups) {
		this.account = account;
		this.accountHostname = accountHostname;
		this.perms.addAll(perms);
		this.groups.addAll(groups);
	}
	
	protected String getPass() {
		return pass;
	}
	
	public List<String> getPerms() {
		return perms;
	}
	
	public boolean isIdentifed() {
		if (nick == null || hostname == null) {
			return identified;
		}
		return (identified || (nick.equalsIgnoreCase(account) && hostname.equalsIgnoreCase(accountHostname)) || ((ChavaPerms.getPermsManager().getMappedAccount(nick) != null) && (ChavaPerms.getPermsManager().getMappedAccount(nick).equalsIgnoreCase(account) && hostname.equalsIgnoreCase(accountHostname))));
	}
	
	public boolean identify(String pass) {
		if (!isIdentifed()) {
			if (pass.equals(this.pass)) {
				identified = true;
				return true;
			}
			return false;
		}
		return true;
	}
	
	public void changePass(String pass) {
		this.pass = pass;
	}
	
	public String getAccount() {
		return account;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public String getAccountHostname() {
		return accountHostname;
	}
	
	public void addPerm(String perm) {
		perms.add(perm);
	}
	
	public void removePerm(String perm) {
		perms.remove(perm);
	}
	
	public boolean hasPerm(String perm) {
		return perms.contains(perm);
	}
	
	public void addGroup(String group) {
		groups.add(group);
	}
	
	public void removeGroup(String group) {
		groups.remove(group);
	}
	
	public List<String> getGroups() {
		return groups;
	}
	
	public boolean inGroup(String group) {
		return groups.contains(group);
	}
}
