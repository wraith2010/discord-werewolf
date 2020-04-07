package com.ten31f.discord.bots;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ten31f.discord.bots.action.AllInAction;
import com.ten31f.discord.bots.action.JoinGameAction;
import com.ten31f.discord.bots.action.KillGameAction;
import com.ten31f.discord.bots.action.StartGameAction;
import com.ten31f.discord.elements.Game;
import com.ten31f.discord.exceptions.NoGameException;
import com.ten31f.discord.repo.GamesRepo;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WereWolfBot extends ListenerAdapter {

	private static final String BOT_USERNAME = "WereWolf";

	private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

	private GamesRepo gamesRepo = null;
	private JoinGameAction joinGameAction = null;
	private StartGameAction startGameAction = null;
	private KillGameAction killGameAction = null;
	private AllInAction allInAction = null;

	public WereWolfBot() {
		setGamesRepo(new GamesRepo());
		setJoinGameAction(new JoinGameAction(getGamesRepo()));
		setStartGameAction(new StartGameAction(getGamesRepo()));
		setKillGameAction(new KillGameAction(getGamesRepo()));
		setAllInAction(new AllInAction(getGamesRepo()));
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent messageReceivedEvent) {

		Message message = messageReceivedEvent.getMessage();

		switch (message.getContentRaw()) {
		case StartGameAction.PHRASE_START_GAME:
			getStartGameAction().process(messageReceivedEvent);
			break;
		case KillGameAction.PHRASE_KILL_GAME:
			getKillGameAction().process(messageReceivedEvent);
			break;
		case JoinGameAction.PHRASE_JOIN_GAME:
			getJoinGameAction().process(messageReceivedEvent);
			break;
		case AllInAction.PHRASE_START_GAME:
			getAllInAction().process(messageReceivedEvent);
			break;
		default:
		}

	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent privateMessageReceivedEvent) {

		String message = privateMessageReceivedEvent.getMessage().getContentRaw();
		String author = privateMessageReceivedEvent.getAuthor().getName();

		// don't read your own message
		if (BOT_USERNAME.equals(author))
			return;

		LOGGER.log(Level.INFO, String.format("%s->%s", author, message));

		try {
			getGamesRepo().processPrivateMessageReceivedEvent(privateMessageReceivedEvent);
		} catch (NoGameException noGameException) {
			privateMessageReceivedEvent.getChannel()
					.sendMessage(NoGameException.generateMessage(privateMessageReceivedEvent.getAuthor())).queue();
		}

	}

	private void setJoinGameAction(JoinGameAction joinGameAction) {
		this.joinGameAction = joinGameAction;
	}

	private JoinGameAction getJoinGameAction() {
		return joinGameAction;
	}

	public Game newGame(MessageChannel messageChannel) {
		return getGamesRepo().newGame(messageChannel);
	}

	private GamesRepo getGamesRepo() {
		return gamesRepo;
	}

	private void setGamesRepo(GamesRepo gamesRepo) {
		this.gamesRepo = gamesRepo;
	}

	private StartGameAction getStartGameAction() {
		return startGameAction;
	}

	private void setStartGameAction(StartGameAction startGameAction) {
		this.startGameAction = startGameAction;
	}

	private KillGameAction getKillGameAction() {
		return killGameAction;
	}

	private void setKillGameAction(KillGameAction killGameAction) {
		this.killGameAction = killGameAction;
	}

	private AllInAction getAllInAction() {
		return allInAction;
	}

	private void setAllInAction(AllInAction allInAction) {
		this.allInAction = allInAction;
	}

}
