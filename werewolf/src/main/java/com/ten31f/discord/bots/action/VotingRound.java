package com.ten31f.discord.bots.action;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ten31f.discord.bots.baseaction.PrivatePromptAction;
import com.ten31f.discord.elements.Game;
import com.ten31f.discord.elements.GameState;
import com.ten31f.discord.elements.Player;

public class VotingRound implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(VotingRound.class.getName());

	private static final String RESPONSE_WAITING = "Waiting on %s players to vote.";

	private Game game = null;

	private Map<String, Integer> votes = null;

	public VotingRound(Game game) {
		setGame(game);
		setVotes(new HashMap<>());
	}

	@Override
	public void run() {

		setup();

		while (!isComplete()) {
			getGame().getMessageChannel()
					.sendMessage(String.format(RESPONSE_WAITING, getGame().getPrivatePromptActions().size()));
			try {
				TimeUnit.SECONDS.sleep(30);
			} catch (InterruptedException interruptedException) {
				Thread.currentThread().interrupt();
			}
		}

		LOGGER.log(Level.INFO, "Voting complete");

		getGame().setGameState(GameState.DAY_CONCLUSION);
	}

	private boolean isComplete() {

		for (PrivatePromptAction privatePromptAction : getGame().getPrivatePromptActions()) {
			if (!privatePromptAction.isSatisfied())
				return false;
		}

		return true;
	}

	private void setup() {

		getGame().clearPrivatePromptActions();

		for (Player player : getGame().getAlivePlayers()) {
			LOGGER.log(Level.INFO, String.format("Setting up vote actoin for %s", player.getUser().getName()));

			VoteAction voteAction = new VoteAction(player.getUser(), getGame(), getVotes());
			new Thread(voteAction).start();
			getGame().addPrivatePromptAction(voteAction);
		}
	}

	private Game getGame() {
		return game;
	}

	private void setGame(Game game) {
		this.game = game;
	}

	private Map<String, Integer> getVotes() {
		return votes;
	}

	private void setVotes(Map<String, Integer> votes) {
		this.votes = votes;
	}

}
