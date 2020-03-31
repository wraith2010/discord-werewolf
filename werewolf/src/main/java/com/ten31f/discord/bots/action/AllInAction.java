package com.ten31f.discord.bots.action;

import com.ten31f.discord.elements.Game;
import com.ten31f.discord.elements.Player;
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

			messageChannel.sendMessage(RESPONSE_LETS_GO).queue();

			game.setupGame();
			
			notifyPlayersOfRoles(game);

		} catch (NoGameException noGameException) {
			messageChannel.sendMessage(NoGameException.generateMessage(user)).queue();
		} catch (NotEnoughPlayersException notEnoughPlayersException) {
			messageChannel.sendMessage(notEnoughPlayersException.getMessage()).queue();
		} catch (GameStateException gameStateException) {
			messageChannel.sendMessage(gameStateException.getMessage()).queue();
		}

	}

	private void notifyPlayersOfRoles(Game game) {

		String msg = "You are are: a %s this game.\nDetails: %s";

		for (Player player : game.getPlayers().values()) {

			player.getUser().openPrivateChannel().queue(channel -> {
				channel.sendMessage(String.format(msg, player.getRole().getName(), player.getRole().getPower()))
						.queue();
			});
		}

	}

	private GamesRepo getGamesRepo() {
		return gamesRepo;
	}

	private void setGamesRepo(GamesRepo gamesRepo) {
		this.gamesRepo = gamesRepo;
	}

}
