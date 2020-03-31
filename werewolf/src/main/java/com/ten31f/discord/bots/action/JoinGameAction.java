package com.ten31f.discord.bots.action;

import com.ten31f.discord.elements.Game;
import com.ten31f.discord.exceptions.NoGameException;
import com.ten31f.discord.repo.GamesRepo;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class JoinGameAction implements ResponseAction {

	public static final String PHRASE_JOIN_GAME = "!join";

	private static final String RESPONSE_PlAYER_ADD = "Woot!: %s is in!!!";
	private static final String RESPONSE_PlAYER_ALREADY = "Relax %s. Your already in.";

	public JoinGameAction(GamesRepo gamesRepo) {
		setGamesRepo(gamesRepo);
	}

	private GamesRepo gamesRepo = null;

	@Override
	public void process(MessageReceivedEvent messageReceivedEvent) {

		MessageChannel messageChannel = messageReceivedEvent.getChannel();
		User user = messageReceivedEvent.getAuthor();

		try {
			Game game = getGamesRepo().getGame(messageChannel);
			if (game.addPlayer(user)) {
				messageChannel.sendMessage(String.format(RESPONSE_PlAYER_ADD, user.getName())).queue();
			} else {
				messageChannel.sendMessage(String.format(RESPONSE_PlAYER_ALREADY, user.getName())).queue();
			}
		} catch (NoGameException noGameException) {
			messageChannel.sendMessage(NoGameException.generateMessage(user)).queue();
		}

	}

	private void setGamesRepo(GamesRepo gamesRepo) {
		this.gamesRepo = gamesRepo;
	}

	private GamesRepo getGamesRepo() {
		return gamesRepo;
	}

}
