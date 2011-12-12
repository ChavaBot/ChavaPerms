package com.alta189.chavaperms;

import com.alta189.chavabot.events.Listener;
import com.alta189.chavabot.events.channelevents.MessageEvent;

public class ChannelMessageListener implements Listener<MessageEvent> {

	public void onEvent(MessageEvent event) {
		ChavaPerms.getPermsManager().identify(event.getUser().getNick(), event.getUser().getNick());
	}

}
