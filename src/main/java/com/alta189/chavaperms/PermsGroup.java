package com.alta189.chavaperms;

import java.util.ArrayList;
import java.util.List;
	
public class PermsGroup {
	private final String name;
	private List<String> perms = new ArrayList<String>();
	
	public PermsGroup(String name) {
		this.name = name;
	}
	
	public PermsGroup(String name, List<String> perms) {
		this.name = name;
		this.perms.addAll(perms);
	}
	
	public List<String> getPerms() {
		return perms;
	}
	
	public String getName() {
		return name;
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
}
