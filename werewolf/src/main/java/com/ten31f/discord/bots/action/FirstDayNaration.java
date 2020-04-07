package com.ten31f.discord.bots.action;

import java.awt.Color;
import java.util.Random;

import com.ten31f.discord.elements.Game;
import com.ten31f.discord.elements.GameState;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

public class FirstDayNaration implements Runnable {

	private static final String[] JOKE_POLICIES = { "Taxes only for the really good looking.",
			"Criminalize movie spoilers.", "To create a holiday for the Sausage McMuffin.",
			"To form a blue ribbon committie to investigate if velveeta really does melt better than cheddar." };

	private static final String TITLE = "The First Morning";
	private static final String PHRASE_MAYOR = "%s is out for a quite morning stroll when the "
			+ "peace of the morning is shattered. In the middle of the town "
			+ "square is the body of the town Mayor Chris P. Bacon. Mayor Bacon was "
			+ "very popular because of his policy \"%s\" So %s calls everyone "
			+ "into the town hall to share the news that there are Werewolves amoung us!";

	private Game game = null;

	public FirstDayNaration(Game game) {
		setGame(game);
	}

	@Override
	public void run() {

		String playerName = getGame().getRandomPlayer().getUser().getName();

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle(TITLE);
		embedBuilder.setColor(Color.ORANGE);
		embedBuilder.setDescription(String.format(PHRASE_MAYOR, playerName, randomPolicy(), playerName));

		MessageChannel messageChannel = getGame().getMessageChannel();

		messageChannel.sendMessage(embedBuilder.build()).complete();

		getGame().setGameState(GameState.DAY_CHAOS);
	}

	private String randomPolicy() {
		Random random = new Random(System.currentTimeMillis());

		return JOKE_POLICIES[random.nextInt(JOKE_POLICIES.length)];

	}

	private Game getGame() {
		return game;
	}

	private void setGame(Game game) {
		this.game = game;
	}

}
