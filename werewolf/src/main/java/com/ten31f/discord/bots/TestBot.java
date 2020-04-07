package com.ten31f.discord.bots;

import com.ten31f.discord.elements.Game;
import com.ten31f.discord.elements.GameState;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TestBot extends ListenerAdapter {

	private static final String ACTIVATION_PHRASE = "!test";

	private static final String RESPONSE_UNRECOGNIZED_TEST = "I don't recognize a test named ' %s'";
	private static final String RESPONSE_NOOPTS = "!test <test> <arguments>";

	private static final String TEST_FIRST_DAY = "firstday";

	private WereWolfBot wereWolfBot = null;
	
	public TestBot(WereWolfBot wereWolfBot) {
		setWereWolfBot(wereWolfBot);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent messageReceivedEvent) {

		Message message = messageReceivedEvent.getMessage();
		MessageChannel messageChannel = messageReceivedEvent.getChannel();
		User user = messageReceivedEvent.getAuthor();

		if (!message.getContentRaw().startsWith(ACTIVATION_PHRASE))
			return;

		if (message.getContentRaw().startsWith(RESPONSE_NOOPTS))
			return;
		
		String messageConent = message.getContentRaw();

		String testName = getTestName(messageConent);

		if(testName == null) {
			messageChannel.sendMessage(RESPONSE_NOOPTS).complete();
			return;
		}
		
		switch (testName) {
		case TEST_FIRST_DAY:
			testFirstDay(messageChannel, user);
			break;
		default:
			messageChannel.sendMessage(String.format(RESPONSE_UNRECOGNIZED_TEST, testName)).complete();
		}
	}

	private void testFirstDay(MessageChannel messageChannel, User user) {

		Game game = getWereWolfBot().newGame(messageChannel);

		game.addPlayer(user);		
		
		game.setGameState(GameState.DAY_ONE_NARRATION);
	}	

	private String getTestName(String messageConent) {

		if (!messageConent.contains(" "))
			return null;

		String testName = messageConent.substring(messageConent.indexOf(' ') + 1);

		if (!testName.contains(" "))
			return testName;

		return testName.substring(0, testName.indexOf(' '));
	}

	private WereWolfBot getWereWolfBot() {
		return wereWolfBot;
	}
	
	private void setWereWolfBot(WereWolfBot wereWolfBot) {
		this.wereWolfBot = wereWolfBot;
	}
	
}
