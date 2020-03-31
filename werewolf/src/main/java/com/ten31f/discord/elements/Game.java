package com.ten31f.discord.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ten31f.discord.bots.baseaction.PrivatePromptAction;
import com.ten31f.discord.exceptions.GameStateException;
import com.ten31f.discord.exceptions.NotEnoughPlayersException;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class Game {

	public static final int MINIMUM_PLAYERS = 2;

	public static enum GameState {
		JOIN, SETUP, DAY_ONE_NARRATION, DAY_NARRATION, DAY_CHAOS, DAY_CONCLUSION, NIGHT, END;
	};

	private Map<String, Player> players = null;
	private MessageChannel messageChannel = null;
	private GameState gameState = GameState.JOIN;
	private List<PrivatePromptAction> privatePromptActions = null;

	public Game(MessageChannel messageChannel) {
		setMessageChannel(messageChannel);
		setPrivatePromptActions(new ArrayList<>());
	}

	public Map<String, Player> getPlayers() {
		return players;
	}

	private void setPlayers(Map<String, Player> players) {
		this.players = players;
	}

	public boolean addPlayer(User user) {

		if (getPlayers() == null)
			setPlayers(new HashMap<String, Player>());

		if (!getPlayers().containsKey(user.getId())) {

			Player player = new Player(user);

			getPlayers().put(user.getId(), player);
			return true;
		}

		return false;
	}

	public int playerCount() {
		if (getPlayers() == null)
			return 0;

		return getPlayers().size();
	}

	public boolean isUserPlaying(User user) {
		if (getPlayers() == null)
			return false;

		for (Player player : getPlayers().values()) {
			if (user.equals(player.getUser()))
				return true;
		}

		return false;
	}

	public void allIn() throws NotEnoughPlayersException, GameStateException {

		if (!GameState.JOIN.equals(getGameState()))
			throw new GameStateException("This game is already started");

		if (playerCount() < MINIMUM_PLAYERS)
			throw new NotEnoughPlayersException(
					String.format("We need more players to start we only have %s/%s", playerCount(), MINIMUM_PLAYERS));

		setGameState(GameState.SETUP);
	}

	/*
	 * Initial game. Assign roles.
	 */
	public void setupGame() {
		// Convert all Map values to a List
		List<Player> playerList = new ArrayList<>(getPlayers().values());
		List<Role> avalibleRoles = new ArrayList<>();

		avalibleRoles.add(Role.SEER);
		avalibleRoles.add(Role.WEREWOLF);
		avalibleRoles.add(Role.WEREWOLF);

		while (avalibleRoles.size() < playerList.size()) {
			avalibleRoles.add(Role.VILLAGER);
		}

		Collections.shuffle(avalibleRoles);

		playerList.forEach(player -> player.setRole(avalibleRoles.remove(0)));
	}

	public void processPrivateMessageReceivedEvent(PrivateMessageReceivedEvent privateMessageReceivedEvent) {
		for (PrivatePromptAction privatePromptAction : getPrivatePromptActions()) {
			if (privateMessageReceivedEvent.getChannel().equals(privatePromptAction.getPrivateChannel())) {
				privatePromptAction.processPrivateMessageReceivedEvent(privateMessageReceivedEvent);
			}
		}
	}

	public MessageChannel getMessageChannel() {
		return messageChannel;
	}

	public void setMessageChannel(MessageChannel messageChannel) {
		this.messageChannel = messageChannel;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {

		this.gameState = gameState;

//		switch (getGameState()) {
//		case DAY_CHAOS:
//			break;
//		case DAY_CONCLUSION:
//			break;
//		case DAY_NARRATION:
//			break;
//		case DAY_ONE_NARRATION:
//			break;
//		case END:
//			break;
//		case JOIN:
//			break;
//		case NIGHT:
//			break;
//		case SETUP:
//			setupGame();
//			break;
//		default:
//			break;
//		}

	}

	public List<PrivatePromptAction> getPrivatePromptActions() {
		return privatePromptActions;
	}

	private void setPrivatePromptActions(List<PrivatePromptAction> privatePromptActions) {
		this.privatePromptActions = privatePromptActions;
	}

	public void addPrivatePromptAction(PrivatePromptAction privatePromptAction) {
		getPrivatePromptActions().add(privatePromptAction);
	}

	public void clearPrivatePromptActions() {
		getPrivatePromptActions().clear();
	}

}
