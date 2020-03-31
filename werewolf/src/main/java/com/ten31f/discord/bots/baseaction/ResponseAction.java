package com.ten31f.discord.bots.baseaction;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ResponseAction {

	/**
	 * React to message from chat
	 * 
	 * @param messageReceivedEvent
	 */
	public void process(MessageReceivedEvent messageReceivedEvent);
	
}
