package com.ten31f.discord;

import javax.security.auth.login.LoginException;

import com.ten31f.discord.bots.PingPongBot;
import com.ten31f.discord.bots.WereWolfBot;
import com.ten31f.discord.eventlisteners.ReadyListener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;

public class App {

	public static void main(String[] args) throws LoginException, InterruptedException {

		JDABuilder builder = JDABuilder.createDefault(args[0]);

		// Enable the bulk delete event
		builder.setBulkDeleteSplittingEnabled(false);
		// Disable compression (not recommended)
		builder.setCompression(Compression.NONE);
		// Set activity (like "playing Something")
		builder.setActivity(Activity.watching("Paint Dry"));

		builder.addEventListeners(new ReadyListener());
		builder.addEventListeners(new PingPongBot());
		builder.addEventListeners(new WereWolfBot());

		JDA jda = builder.build();

		jda.awaitReady();

	}
}
