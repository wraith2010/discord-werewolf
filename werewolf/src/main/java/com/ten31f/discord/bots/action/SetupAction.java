package com.ten31f.discord.bots.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ten31f.discord.elements.Game;
import com.ten31f.discord.elements.GameState;
import com.ten31f.discord.elements.Player;
import com.ten31f.discord.elements.Role;

public class SetupAction implements Runnable {

	private Game game = null;

	public SetupAction(Game game) {
		setGame(game);
	}

	@Override
	public void run() {
		// Convert all Map values to a List
		List<Player> playerList = new ArrayList<>(getGame().getPlayers().values());
		List<Role> avalibleRoles = new ArrayList<>();

		avalibleRoles.add(Role.SEER);
		avalibleRoles.add(Role.WEREWOLF);
		avalibleRoles.add(Role.WEREWOLF);

		while (avalibleRoles.size() < playerList.size()) {
			avalibleRoles.add(Role.VILLAGER);
		}

		Collections.shuffle(avalibleRoles);

		playerList.forEach(player -> player.setRole(avalibleRoles.remove(0)));

		notifyPlayersOfRoles();
		
		getGame().setGameState(GameState.DAY_ONE_NARRATION);
	}
	
	private void notifyPlayersOfRoles() {

		String msg = "You are are: a %s this game.\nDetails: %s";

		for (Player player : getGame().getPlayers().values()) {

			player.getUser().openPrivateChannel().queue(channel -> {
				channel.sendMessage(String.format(msg, player.getRole().getName(), player.getRole().getPower()))
						.queue();
			});
		}

	}

	private Game getGame() {
		return game;
	}

	private void setGame(Game game) {
		this.game = game;
	}

}
