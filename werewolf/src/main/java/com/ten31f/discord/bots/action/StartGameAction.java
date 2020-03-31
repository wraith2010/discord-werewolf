package com.ten31f.discord.bots.action;

import com.ten31f.discord.elements.Game;
import com.ten31f.discord.repo.GamesRepo;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StartGameAction implements ResponseAction {

	public static final String PHRASE_START_GAME = "!play werewolf";

	private static final String RESPONSE_STARTING = "Pitter patter lets get at’er!";
	private static final String RESPONSE_ALREADY_PLAYING = "Sorry only one game of werewolf at time per channel.";

	private GamesRepo gamesRepo = null;

	public StartGameAction(GamesRepo gamesRepo) {
		setGamesRepo(gamesRepo);
	}

	@Override
	public void process(MessageReceivedEvent messageReceivedEvent) {

		MessageChannel messageChannel = messageReceivedEvent.getChannel();

		Game game = getGamesRepo().newGame(messageChannel);
		if (game == null) {
			messageChannel.sendMessage(RESPONSE_ALREADY_PLAYING).queue();
			return;
		}

		User user = messageReceivedEvent.getAuthor();

		game.addPlayer(user);

		StringBuilder stringBuilder = new StringBuilder(RESPONSE_STARTING);
		stringBuilder.append("\n");
		stringBuilder.append(String.format("Who's in beside: %s\n", user.getName()));
		stringBuilder.append(String.format("We need atleast %s\n", Game.MINIMUM_PLAYERS));
		stringBuilder.append("Type: !join to join in you have 2 minutes to join");

		messageChannel.sendMessage(stringBuilder).queue();
	}

	private GamesRepo getGamesRepo() {
		return gamesRepo;
	}

	private void setGamesRepo(GamesRepo gamesRepo) {
		this.gamesRepo = gamesRepo;
	}

}
