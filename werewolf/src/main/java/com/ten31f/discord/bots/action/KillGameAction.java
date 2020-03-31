package com.ten31f.discord.bots.action;

import com.ten31f.discord.bots.baseaction.ResponseAction;
import com.ten31f.discord.exceptions.NoGameException;
import com.ten31f.discord.repo.GamesRepo;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class KillGameAction implements ResponseAction {

	public static final String PHRASE_KILL_GAME = "!end werewolf";

	private static final String RESPONSE_KILL_GAME = "%s says its time to go home. Game over!";

	private GamesRepo gamesRepo = null;

	public KillGameAction(GamesRepo gamesRepo) {
		setGamesRepo(gamesRepo);
	}

	@Override
	public void process(MessageReceivedEvent messageReceivedEvent) {
		MessageChannel messageChannel = messageReceivedEvent.getChannel();

		User user = messageReceivedEvent.getAuthor();

		try {
			getGamesRepo().removeGame(messageChannel);

			messageChannel.sendMessage(String.format(RESPONSE_KILL_GAME, user.getName())).queue();

		} catch (NoGameException noGameException) {
			messageChannel.sendMessage(NoGameException.generateMessage(user)).queue();
			return;
		}

	}

	private GamesRepo getGamesRepo() {
		return gamesRepo;
	}

	private void setGamesRepo(GamesRepo gamesRepo) {
		this.gamesRepo = gamesRepo;
	}

}
