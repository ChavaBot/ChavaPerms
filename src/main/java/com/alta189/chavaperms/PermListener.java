package com.alta189.chavaperms;

import java.util.StringTokenizer;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import com.alta189.chavabot.ChavaManager;
import com.alta189.chavaperms.db.data.Account;
import com.alta189.chavaperms.db.data.Group;
import com.alta189.chavaperms.db.data.WhitelistedAccount;

@SuppressWarnings("rawtypes")
public class PermListener extends ListenerAdapter {

	public class PermsListener extends ListenerAdapter {

		@Override
		public void onJoin(JoinEvent event) throws Exception {
			ChavaPerms.getPermsManager().identify(event.getUser().getNick(), event.getUser().getNick());
		}

		@Override
		public void onMessage(MessageEvent event) throws Exception {
			ChavaPerms.getPermsManager().identify(event.getUser().getNick(), event.getUser().getNick());
		}

		@Override
		public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
			ChavaPerms.getPermsManager().identify(event.getUser().getNick(), event.getUser().getNick());
			String sender = event.getUser().getNick();
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
					Account user = new Account();
					user.setAccount(sender.toLowerCase());
					user.setHostname(event.getUser().getHostmask().toLowerCase());
					user.setPassword(pass.toLowerCase());
					user.addGroup("default");
					user.addPerm("perms");
					ChavaPerms.getPermsManager().addAccount(user);
				} else if (command.equalsIgnoreCase("group")) {
					if (!tokens.hasMoreTokens() || tokensCount != 3) {
						ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid syntax. group account <pass>");
						return;
					}
					if (ChavaPerms.getPermsManager().hasAccount(sender)) {
						ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You already are mapped to an account.");
						return;
					}

					String name = tokens.nextToken().toLowerCase();
					String pass = Utils.getMD5Hash(tokens.nextToken());
					WhitelistedAccount wa = ChavaPerms.getPermsManager().getWhitelistedAccount(sender);
					if (wa != null) {
						ChavaManager.getInstance().getChavaBot().sendMessage(sender, "You already are mapped to an account, but we can't link it to a valid account.");
						return;					
					}

					Account account = ChavaPerms.getPermsManager().getAccount(name);
					if (account == null) {
						ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid account.");
						return;					
					}

					if (!account.getPassword().equalsIgnoreCase(pass)) {
						ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Incorrect Password.");
						return;
					}


					wa = new WhitelistedAccount();
					wa.setAccount(name);
					wa.setName(sender);
					wa.setHostname(event.getUser().getHostmask());
					ChavaPerms.getPermsManager().saveWhitelistedAccount(wa);
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
						String perm = tokens.nextToken().toLowerCase();
						ChavaPerms.getPermsManager().getAccount(account).addPerm(perm.toLowerCase());
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
						String perm = tokens.nextToken().toLowerCase();
						ChavaPerms.getPermsManager().getAccount(account).remPerm(perm);
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
						String group = tokens.nextToken().toLowerCase();
						ChavaPerms.getPermsManager().getAccount(account).remGroup(group);
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
					if (!ChavaPerms.getPermsManager().validGroup(group)) {
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
					String group = tokens.nextToken().toLowerCase();
					if (!ChavaPerms.getPermsManager().validGroup(group)) {
						ChavaManager.getInstance().getChavaBot().sendMessage(sender, "Invalid group.");
						return;
					}

					while (tokens.hasMoreElements()) {
						String perm = tokens.nextToken().toLowerCase();
						ChavaPerms.getPermsManager().getGroup(group).remPerm(perm);
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
					if (ChavaPerms.getPermsManager().validGroup(group)) {
						ChavaManager.getInstance().getChavaBot().sendMessage(sender, "This group already exists.");
						return;
					}
					Group pg = new Group();
					pg.setGroup(group.toLowerCase());
					pg.addPerm("perm");
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
					String perm = tokens.nextToken().toLowerCase();
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
				} else {
					ChavaManager.getInstance().getChavaBot().sendMessage(sender, "The commands are register, identify, gadd, grem, gpadd, gprem, ngroup, add, hasperm, hasaccount, and rem.");
					return;
				}
			}
		}



	}

}
