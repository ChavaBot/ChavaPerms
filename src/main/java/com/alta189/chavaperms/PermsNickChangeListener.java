package com.alta189.chavaperms;

import com.alta189.chavabot.events.Listener;
import com.alta189.chavabot.events.userevents.NickChangeEvent;

public class PermsNickChangeListener implements Listener<NickChangeEvent> {

	public void onEvent(NickChangeEvent event) {
		String mapped = ChavaPerms.getPermsManager().getMappedAccount(event.getOldNick().toLowerCase());
		if (mapped != null) {
			ChavaPerms.getPermsManager().removeMap(event.getOldNick().toLowerCase());
			ChavaPerms.getPermsManager().mapAccount(event.getNewNick().toLowerCase(), mapped);
		}
	}

}
