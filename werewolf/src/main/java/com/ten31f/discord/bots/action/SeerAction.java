package com.ten31f.discord.bots.action;

import java.util.List;
import java.util.stream.Collectors;

import com.ten31f.discord.bots.baseaction.PrivatePromptAction;
import com.ten31f.discord.elements.Game;
import com.ten31f.discord.elements.Player;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class SeerAction extends PrivatePromptAction {

	private static final String PROMPT_SEER_CHECK = "Okay Seer who do you want to check?";

	private List<Player> targetPlayers = null;

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

		getPrivateChannel().sendMessage(stringBuilder.toString()).complete();
	}
	
	@Override
	public void processPrivateMessageReceivedEvent(PrivateMessageReceivedEvent privateMessageReceivedEvent) {
		// TODO Auto-generated method stub
		
	}

	private List<Player> getTargetPlayers() {
		return targetPlayers;
	}

	private void setTargetPlayers(List<Player> targetPlayers) {
		this.targetPlayers = targetPlayers;
	}

}
