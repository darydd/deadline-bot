package com.dd.bot;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

	private final DatabaseManager db;

	public CommandListener(DatabaseManager db) {
		this.db = db;
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (event.getName().equals("ping")) {
			event.reply("Pong!").queue();
		}
		if (event.getName().equals("adddeadline")) {
			handleAddDeadline(event);
		}
		if (event.getName().equals("deadlines")) {
			handleDeadlines(event);
		}
	}

	private void handleAddDeadline(SlashCommandInteractionEvent event) {
		String module = event.getOption("module").getAsString();
		String title = event.getOption("title").getAsString();
		String dateText = event.getOption("date").getAsString();

		LocalDate dueDate;
		try {
			dueDate = LocalDate.parse(dateText);
		} catch (DateTimeParseException e) {
			event.reply("Invalid date. Use YYYY-MM-DD").setEphemeral(true).queue();
			return;
		}

		if (dueDate.isBefore(LocalDate.now())) {
			event.reply("That date is in the past").setEphemeral(true).queue();
			return;
		}

		try {
			db.addDeadline(event.getGuild().getId(), module, title, dueDate, event.getUser().getId());
			event.reply("Added **" + module + "**: " + title + " (due " + dueDate + ")").queue();
		} catch (SQLException e) {
			e.printStackTrace();
			event.reply("Something went wrong with saving the deadline").setEphemeral(true).queue();
		}
	}

	private void handleDeadlines(SlashCommandInteractionEvent event) {
		try {
			List<Deadline> deadlines = db.getUpcomingDeadlines(event.getGuild().getId());

			if (deadlines.isEmpty()) {
				event.reply("No upcoming deadlines.").queue();
				return;
			}

			StringBuilder message = new StringBuilder("**Upcoming deadlines**\n");
			for (Deadline d : deadlines) {
				message.append("• **").append(d.module()).append("**: ").append(d.title()).append(" (due ")
						.append(d.dueDate()).append(")\n");
			}

			event.reply(message.toString()).queue();
		} catch (SQLException e) {
			e.printStackTrace();
			event.reply("Something went wrong loading deadlines").setEphemeral(true).queue();

		}

	}
}