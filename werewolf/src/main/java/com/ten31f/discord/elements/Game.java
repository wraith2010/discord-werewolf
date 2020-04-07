package com.ten31f.discord.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ten31f.discord.bots.action.FirstDayNaration;
import com.ten31f.discord.bots.action.SetupAction;
import com.ten31f.discord.bots.action.VotingRound;
import com.ten31f.discord.bots.baseaction.PrivatePromptAction;
import com.ten31f.discord.exceptions.GameStateException;
import com.ten31f.discord.exceptions.NotEnoughPlayersException;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class Game {

	private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

	public static final int MINIMUM_PLAYERS = 2;

	private Random random = null;

	private MessageChannel messageChannel = null;
	private GameState gameState = GameState.JOIN;	
	private List<PrivatePromptAction> privatePromptActions = null;

	private Map<String, Player> players = null;

	public Game(MessageChannel messageChannel) {
		setMessageChannel(messageChannel);
		setPrivatePromptActions(new ArrayList<>());
		setRandom(new Random(System.currentTimeMillis()));
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

	public void processPrivateMessageReceivedEvent(PrivateMessageReceivedEvent privateMessageReceivedEvent) {

		for (Iterator<PrivatePromptAction> iterator = getPrivatePromptActions().iterator(); iterator.hasNext();) {
			PrivatePromptAction privatePromptAction = iterator.next();
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

		switch (gameState) {
		case DAY_CHAOS:
			new Thread(new VotingRound(this)).start();
			break;
		case DAY_CONCLUSION:
			break;
		case DAY_NARRATION:
			break;
		case DAY_ONE_NARRATION:
			new Thread(new FirstDayNaration(this)).start();
			break;
		case END:
			break;
		case JOIN:
			break;
		case NIGHT:
			break;
		case SETUP:
			new Thread(new SetupAction(this)).start();
			break;
		default:
			break;
		}

	}

	public Player getRandomPlayer() {

		int index = getRandom().nextInt(getPlayers().size());

		return (Player) getPlayers().values().toArray()[index];

	}

	public Player getRandomAlivePlayer() {

		List<Player> alivePlayers = getAlivePlayers();

		int index = getRandom().nextInt(alivePlayers.size());

		return alivePlayers.get(index);
	}

	public List<Player> getAlivePlayers() {
		return getPlayers().values().stream().filter(Player::isAlive).collect(Collectors.toList());
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

	public void removePrivatePromptAction(PrivatePromptAction privatePromptAction) {
		getPrivatePromptActions().remove(privatePromptAction);
	}

	public void clearPrivatePromptActions() {
		getPrivatePromptActions().clear();
	}

	private Random getRandom() {
		return random;
	}

	private void setRandom(Random random) {
		this.random = random;
	}

}
