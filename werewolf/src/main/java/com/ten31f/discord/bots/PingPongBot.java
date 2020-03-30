package com.ten31f.discord.bots;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingPongBot extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent messageReceivedEvent) {

		Message message = messageReceivedEvent.getMessage();
		if (message.getContentRaw().equals("!ping")) {
			MessageChannel channel = messageReceivedEvent.getChannel();

			User user = messageReceivedEvent.getAuthor();

			//user.hasPrivateChannel()
			
			long time = System.currentTimeMillis();
			channel.sendMessage("Pong!") /* => RestAction<Message> */
					.queue(response /* => Message */ -> {
						response.editMessageFormat("Pong: %s %d ms", user.getName(), System.currentTimeMillis() - time)
								.queue();
					});
		}
	}

}
