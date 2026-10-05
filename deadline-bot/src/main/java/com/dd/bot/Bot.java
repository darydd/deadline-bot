package com.dd.bot;

import net.dv8tion.jda.api.JDA;
import java.sql.SQLException;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Bot {
	   public static void main(String[] args) throws InterruptedException, SQLException {
        System.out.println("Starting bot");

        String token = System.getenv("DISCORD_TOKEN");
        if (token == null) {
            System.err.println("DISCORD_TOKEN not found");
            return;
        }
        
        DatabaseManager db = new DatabaseManager();

        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new CommandListener(db))
                .build();

        System.out.println("Waiting for connection");
        jda.awaitReady();
        System.out.println("Connected");

        Guild guild = jda.getGuildById(System.getenv("GUILD_ID"));
        guild.updateCommands()
        .addCommands(
            Commands.slash("ping", "Check the bot is alive"),

            Commands.slash("adddeadline", "Adds a new deadline")
                    .addOption(OptionType.STRING, "module", "Module name, e.g. Algorithms", true)
                    .addOption(OptionType.STRING, "title", "What's due, e.g. Coursework 1", true)
                    .addOption(OptionType.STRING, "date", "Due date in YYYY-MM-DD format", true),

            Commands.slash("deadlines", "Show upcoming deadlines")
        )
        .queue();

        System.out.println("Commands registered");
    }
}