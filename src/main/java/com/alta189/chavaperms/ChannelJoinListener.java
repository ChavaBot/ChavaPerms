package com.alta189.chavaperms;

import com.alta189.chavabot.events.Listener;
import com.alta189.chavabot.events.channelevents.ChannelJoinEvent;

public class ChannelJoinListener implements Listener<ChannelJoinEvent> {

	public void onEvent(ChannelJoinEvent event) {
		ChavaPerms.getPermsManager().hostIdentify(event.getUser().getNick(), event.getUser().getNick(), event.getUser().getHostname());
	}

}
