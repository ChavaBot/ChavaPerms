package com.alta189.chavaperms.db.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "accounts")
public class Account {
	
	@Id
	private int id;
	
	@NotNull
	private String account;
	
	@NotNull
	private String hostname;
	
	@NotNull
	private String password;
	
	@NotNull
	private List<String> perms;
	
	@NotNull 
	private List<String> groups;
	
	@NotNull 
	private boolean identified;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getPerms() {
		return perms;
	}

	public void setPerms(List<String> perms) {
		this.perms = perms;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public boolean isIdentified() {
		return identified;
	}

	public void setIdentified(boolean identified) {
		this.identified = identified;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public boolean hasPerm(String perm) {
		perm = perm.toLowerCase();
		return perms.contains(perm);
	}
	
	public void addPerm(String perm) {
		perm = perm.toLowerCase();
		perms.add(perm);
	}
	
	public void remPerm(String perm) {
		perm = perm.toLowerCase();
		perms.remove(perm);
	}
	
	public boolean inGroup(String group) {
		group = group.toLowerCase();
		return groups.contains(group);
	}
	
	public void addGroup(String group) {
		group = group.toLowerCase();
		groups.add(group);
	}
	
	public void remGroup(String group) {
		group = group.toLowerCase();
		groups.remove(group);
	}
}
