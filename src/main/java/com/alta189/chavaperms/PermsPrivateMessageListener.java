package com.alta189.chavaperms;

import java.util.StringTokenizer;

import com.alta189.chavabot.ChavaManager;
import com.alta189.chavabot.events.Listener;
import com.alta189.chavabot.events.botevents.PrivateMessageEvent;

public class PermsPrivateMessageListener implements Listener<PrivateMessageEvent> {

	public void onEvent(PrivateMessageEvent event) {
		String sender = event.getSender().getNick();
		StringTokenizer tokens = new StringTokenizer(event.getMessage());
		if (tokens.countTokens() < 2) {
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
				String pass = tokens.nextToken();
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
				if (tokens.countTokens() == 3) {
					account = tokens.nextToken();
					pass = tokens.nextToken();
				} else {
					account = sender;
					pass = tokens.nextToken();
				}
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
				if (ChavaPerms.getPermsManager().hasPerms(sender, "perms.add"));
				if (!tokens.hasMoreTokens() || tokens.countTokens() < 3) {
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
				if (ChavaPerms.getPermsManager().hasPerms(sender, "perms.rem"));
				if (!tokens.hasMoreTokens() || tokens.countTokens() < 3) {
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
				
			} else {
				ChavaManager.getInstance().getChavaBot().sendMessage(sender, "The commands are register, identify, add, and rem.");
				return;
			}
		}
	}

}
