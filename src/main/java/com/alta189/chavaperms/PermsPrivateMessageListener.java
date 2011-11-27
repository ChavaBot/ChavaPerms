package com.alta189.chavaperms;

import java.util.StringTokenizer;

import com.alta189.chavabot.ChavaManager;
import com.alta189.chavabot.events.Listener;
import com.alta189.chavabot.events.botevents.PrivateMessageEvent;

public class PermsPrivateMessageListener implements Listener<PrivateMessageEvent> {

	public void onEvent(PrivateMessageEvent event) {
		String sender = event.getSender().getNick();
		if (!event.getMessage().startsWith(".perms "))
			return;
		StringTokenizer tokens = new StringTokenizer(event.getMessage().replaceFirst(".perms ", ""));
		int tokensCount = tokens.countTokens();
		if (tokensCount < 2) {
			if (tokens.nextToken().equalsIgnoreCase("help")) {
				ChavaManager.getInstance().getChavaBot().sendMessage(sender, "The commands are register, identify, add, and rem.");
				return;
			}
			ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid Command");
		} else {
			String command = tokens.nextToken();
			if (command.equalsIgnoreCase("register")) {
				if (!tokens.hasMoreTokens()) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. register <pass>");
					return;
				}
				if (ChavaPerms.getPermsManager().hasAccount(sender)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You already have an account.");
					return;
				}
				String pass = Utils.getMD5Hash(tokens.nextToken());
				PermsUser user = new PermsUser(sender, event.getSender().getHostname(), pass);
				user.addGroup("default");
				user.addPerm("perms");
				ChavaPerms.getPermsManager().addAccount(user);
			} else if (command.equalsIgnoreCase("identify")) {
				if (!tokens.hasMoreTokens()) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. identify [account] <pass>");
					return;
				}
				String account = null;
				String pass = null;
				if (tokensCount == 3) {
					account = tokens.nextToken();
					pass = tokens.nextToken();
				} else {
					account = sender;
					pass = tokens.nextToken();
				}
				pass = Utils.getMD5Hash(pass);
				if (!ChavaPerms.getPermsManager().hasAccount(account)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid account.");
					return;
				}
				if (ChavaPerms.getPermsManager().identify(account, pass)) {
					if (!account.equalsIgnoreCase(sender)) {
						ChavaPerms.getPermsManager().mapAccount(sender, account);
					}
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You are now identified for " + account);
				} else {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Unable to Identify you for " + account);
				}
			} else if (command.equalsIgnoreCase("add")) {
				if (ChavaPerms.getPermsManager().hasPerms(sender, "perms.add")) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You don't have permission");
					return;
				}
				if (!tokens.hasMoreTokens() || tokensCount < 3) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. add <account> perm1 [perm2] [perm3]");
					return;
				}
				String account = tokens.nextToken();
				if (!ChavaPerms.getPermsManager().hasAccount(account)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid account.");
					return;
				}

				while (tokens.hasMoreElements()) {
					String perm = tokens.nextToken();
					ChavaPerms.getPermsManager().getAccount(account).addPerm(perm);
				}

			} else if (command.equalsIgnoreCase("rem")) {
				if (!ChavaPerms.getPermsManager().hasPerms(sender, "perms.rem")) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You don't have permission");
					return;
				}
				if (!tokens.hasMoreTokens() || tokensCount < 3) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. rem <account> perm1 [perm2] [perm3]");
					return;
				}
				String account = tokens.nextToken();
				if (!ChavaPerms.getPermsManager().hasAccount(account)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid account.");
					return;
				}

				while (tokens.hasMoreElements()) {
					String perm = tokens.nextToken();
					ChavaPerms.getPermsManager().getAccount(account).removePerm(perm);
				}

			} else if (command.equalsIgnoreCase("gadd")) {
				if (!ChavaPerms.getPermsManager().hasPerms(sender, "perms.gadd")) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You don't have permission");
					return;
				}
				if (!tokens.hasMoreTokens() || tokensCount < 3) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. gadd <account> group1 [group2] [group3]");
					return;
				}
				String account = tokens.nextToken();
				if (!ChavaPerms.getPermsManager().hasAccount(account)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid account.");
					return;
				}

				while (tokens.hasMoreElements()) {
					String group = tokens.nextToken();
					ChavaPerms.getPermsManager().getAccount(account).addGroup(group);
				}

			} else if (command.equalsIgnoreCase("grem")) {
				if (!ChavaPerms.getPermsManager().hasPerms(sender, "perms.grem")) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You don't have permission");
					return;
				}
				if (!tokens.hasMoreTokens() || tokensCount < 3) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. grem <account> group1 [group2] [group3]");
					return;
				}
				String account = tokens.nextToken();
				if (!ChavaPerms.getPermsManager().hasAccount(account)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid account.");
					return;
				}

				while (tokens.hasMoreElements()) {
					String group = tokens.nextToken();
					ChavaPerms.getPermsManager().getAccount(account).removeGroup(group);
				}
			} else if (command.equalsIgnoreCase("gpadd")) {
				if (!ChavaPerms.getPermsManager().hasPerms(sender, "perms.gpadd")) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You don't have permission");
					return;
				}
				if (!tokens.hasMoreTokens() || tokensCount < 3) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. gpadd <group> perm1 [perm2] [perm3]");
					return;
				}
				String group = tokens.nextToken();
				if (!ChavaPerms.getPermsManager().isValidGroup(group)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid group.");
					return;
				}

				while (tokens.hasMoreElements()) {
					String perm = tokens.nextToken();
					ChavaPerms.getPermsManager().getGroup(group).addPerm(perm);
				}
			} else if (command.equalsIgnoreCase("gprem")) {
				if (!ChavaPerms.getPermsManager().hasPerms(sender, "perms.gprem")) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You don't have permission");
					return;
				}
				if (!tokens.hasMoreTokens() || tokensCount < 3) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. gprem <group> perm1 [perm2] [perm3]");
					return;
				}
				String group = tokens.nextToken();
				if (!ChavaPerms.getPermsManager().isValidGroup(group)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid group.");
					return;
				}

				while (tokens.hasMoreElements()) {
					String perm = tokens.nextToken();
					ChavaPerms.getPermsManager().getGroup(group).removePerm(perm);
				}
			} else if (command.equalsIgnoreCase("ngroup")) {
				if (!ChavaPerms.getPermsManager().hasPerms(sender, "perms.ngroup")) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You don't have permission");
					return;
				}
				if (!tokens.hasMoreTokens() || tokensCount < 2) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. ngroup <new group name>");
					return;
				}
				String group = tokens.nextToken();
				if (ChavaPerms.getPermsManager().isValidGroup(group)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "This group already exists.");
					return;
				}
				PermsGroup pg = new PermsGroup(group);
				ChavaPerms.getPermsManager().addGroup(pg);
			} else if (command.equalsIgnoreCase("hasperm")) {
				if (!ChavaPerms.getPermsManager().hasPerms(sender, "perms.hasperm")) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You don't have permission");
					return;
				}
				if (!tokens.hasMoreTokens() || tokensCount != 3) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. hasperm <account> perm");
					return;
				}
				String account = tokens.nextToken();
				if (!ChavaPerms.getPermsManager().hasAccount(account)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid account.");
					return;
				}
				String perm = tokens.nextToken();
				if (ChavaPerms.getPermsManager().hasPerms(account, perm)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, new StringBuilder().append("Account '").append(account).append("' does have perm '").append(perm).append("'").toString());
				} else {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, new StringBuilder().append("Account '").append(account).append("' does not have perm '").append(perm).append("'").toString());
				}
			} else if (command.equalsIgnoreCase("hasaccount")) {
				if (!ChavaPerms.getPermsManager().hasPerms(sender, "perms.hasaccount")) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You don't have permission");
					return;
				}
				if (!tokens.hasMoreTokens() || tokensCount != 2) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. hasaccount <account>");
					return;
				}
				String account = tokens.nextToken();
				if (ChavaPerms.getPermsManager().hasAccount(account)) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, new StringBuilder().append("Account '").append(account).append("' exists").toString());
				} else {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, new StringBuilder().append("Account '").append(account).append("' does not exist").toString());
				}
			} else if (command.equalsIgnoreCase("maccount")) {
				if (!ChavaPerms.getPermsManager().hasPerms(sender, "perms.maccount")) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You don't have permission");
					return;
				}
				if (!tokens.hasMoreTokens() || tokensCount != 2) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. maccount <nick>");
					return;
				}
				String nick = tokens.nextToken();
				String mapped = ChavaPerms.getPermsManager().getMappedAccount(nick);
				if (mapped == null) {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, new StringBuilder().append("Nick '").append(nick).append("' is not mapped to an account").toString());
				} else {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, new StringBuilder().append("Nick '").append(nick).append("' is  mapped to Account '").append(mapped).append("'").toString());					
				}
			} else {
				ChavaManager.getInstance().getChavaBot().sendMessage(sender, "The commands are register, identify, gadd, grem, gpadd, gprem, ngroup, add, hasperm, hasaccount, maccount, and rem.");
				return;
			}
		}
	}

}
