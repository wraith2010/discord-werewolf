package com.ten31f.discord.bots.action;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ten31f.discord.bots.baseaction.PrivatePromptAction;
import com.ten31f.discord.elements.Game;
import com.ten31f.discord.elements.Player;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class VoteAction extends PrivatePromptAction implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(VoteAction.class.getName());

	private static final String TITLE = "Town Meeting";
	private static final String PROMPT_VOTE = "Who do you think the Werewolf is?";
	private static final String OPTION_NO_KILL = "No kill!";

	private Map<String, Integer> votes;

	public VoteAction(User user, Game game, Map<String, Integer> votes) {
		super(user, game);

		setVotes(votes);

		// make a list of players who are not the prompted player and are still alive
		setTargetPlayers(game.getPlayers().values().stream()
				.filter(player -> !user.equals(player.getUser()) && player.isAlive()).collect(Collectors.toList()));
	}

	@Override
	public void run() {

		prompt();
		while (!isSatisfied()) {
			try {
				TimeUnit.SECONDS.sleep(15);
			} catch (InterruptedException ignored) {

			}
		}

		LOGGER.log(Level.INFO, String.format("Voting satisfied for %s", getUser().getName()));
	}

	@Override
	public void prompt() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(PROMPT_VOTE);
		stringBuilder.append("\n");
		int index = 1;

		stringBuilder.append(String.format("%s.) %s\n", index, OPTION_NO_KILL));

		for (Player player : getTargetPlayers()) {
			index++;
			stringBuilder.append(String.format("%s.) %s\n", index, player.getUser().getName()));
		}

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle(TITLE);
		embedBuilder.setColor(Color.CYAN);
		embedBuilder.setDescription(stringBuilder.toString());

		getPrivateChannel().sendMessage(embedBuilder.build()).complete();

	}

	@Override
	public void processPrivateMessageReceivedEvent(PrivateMessageReceivedEvent privateMessageReceivedEvent) {

		PrivateChannel privateChannel = privateMessageReceivedEvent.getChannel();

		Message message = privateMessageReceivedEvent.getMessage();
		String messageString = message.getContentRaw();
		try {
			int selection = Integer.parseInt(messageString);
			if (selection < 1 || selection > (getTargetPlayers().size() + 1)) {
				suggest(privateChannel);
				return;
			}

			if (selection == 1) {
				recordvote(OPTION_NO_KILL);
			} else {
				recordvote(getTargetPlayers().get(selection - 2).getUser().getName());
			}

			setSatisfied(true);

		} catch (NumberFormatException numberFormatException) {
			suggest(privateChannel);
		}

	}

	private synchronized void recordvote(String userName) {
		if (!getVotes().containsKey(userName)) {
			getVotes().put(userName, 1);
			return;
		}

		getVotes().put(userName, getVotes().get(userName) + 1);
	}

	private void suggest(PrivateChannel privateChannel) {
		int limit = getTargetPlayers().size() + 2;

		privateChannel.sendMessage(String.format("Response has to be a number between 1 and %s.", limit)).complete();
	}

	private Map<String, Integer> getVotes() {
		return votes;
	}

	private void setVotes(Map<String, Integer> votes) {
		this.votes = votes;
	}

}
