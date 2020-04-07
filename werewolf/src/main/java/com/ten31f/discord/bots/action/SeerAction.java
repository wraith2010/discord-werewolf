package com.ten31f.discord.bots.action;

import java.awt.Color;
import java.util.stream.Collectors;

import com.ten31f.discord.bots.baseaction.PrivatePromptAction;
import com.ten31f.discord.elements.Game;
import com.ten31f.discord.elements.Player;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class SeerAction extends PrivatePromptAction {

	private static final String TITLE = "Seer Check";
	private static final String PROMPT_SEER_CHECK = "Okay Seer, who do you want to check?";

	public SeerAction(User user, Game game) {
		super(user, game);

		// make a list of players who are not the prompted player and are still alive
		setTargetPlayers(game.getPlayers().values().stream()
				.filter(player -> !user.equals(player.getUser()) && player.isAlive()).collect(Collectors.toList()));

	}

	@Override
	public void prompt() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(PROMPT_SEER_CHECK);
		stringBuilder.append("\n");
		int index = 0;
		for (Player player : getTargetPlayers()) {
			index++;
			stringBuilder.append(String.format("%s.) %s\n", index, player.getUser().getName()));
		}

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle(TITLE);
		embedBuilder.setColor(Color.CYAN);
		embedBuilder.setDescription(stringBuilder.toString());

		getPrivateChannel().sendMessage(embedBuilder.build()).queue();
	}

	@Override
	public void processPrivateMessageReceivedEvent(PrivateMessageReceivedEvent privateMessageReceivedEvent) {
		

	}

}
