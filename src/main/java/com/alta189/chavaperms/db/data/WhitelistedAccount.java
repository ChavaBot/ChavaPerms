package com.alta189.chavaperms.db.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "whitelist")
public class WhitelistedAccount {

	@Id
	private int id;
	
	@NotNull
	private String name;
	
	@NotNull 
	private String hostname;
	
	@NotNull
	private String account;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
