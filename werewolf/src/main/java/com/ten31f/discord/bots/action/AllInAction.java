package com.ten31f.discord.bots.action;

import com.ten31f.discord.bots.baseaction.ResponseAction;
import com.ten31f.discord.elements.Game;
import com.ten31f.discord.elements.GameState;
import com.ten31f.discord.exceptions.GameStateException;
import com.ten31f.discord.exceptions.NoGameException;
import com.ten31f.discord.exceptions.NotEnoughPlayersException;
import com.ten31f.discord.repo.GamesRepo;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AllInAction implements ResponseAction {

	public static final String PHRASE_START_GAME = "!all in";

	private static final String RESPONSE_LETS_GO = "Everyone's in, lets get started.";

	private GamesRepo gamesRepo = null;

	public AllInAction(GamesRepo gamesRepo) {
		setGamesRepo(gamesRepo);
	}

	@Override
	public void process(MessageReceivedEvent messageReceivedEvent) {

		MessageChannel messageChannel = messageReceivedEvent.getChannel();
		User user = messageReceivedEvent.getAuthor();

		try {
			Game game = getGamesRepo().getGame(messageChannel);
			game.allIn();

			messageChannel.sendMessage(RESPONSE_LETS_GO).complete();

			game.setGameState(GameState.SETUP);

		} catch (NoGameException noGameException) {
			messageChannel.sendMessage(NoGameException.generateMessage(user)).queue();
		} catch (NotEnoughPlayersException notEnoughPlayersException) {
			messageChannel.sendMessage(notEnoughPlayersException.getMessage()).queue();
		} catch (GameStateException gameStateException) {
			messageChannel.sendMessage(gameStateException.getMessage()).queue();
		}

	}

	private GamesRepo getGamesRepo() {
		return gamesRepo;
	}

	private void setGamesRepo(GamesRepo gamesRepo) {
		this.gamesRepo = gamesRepo;
	}

}
