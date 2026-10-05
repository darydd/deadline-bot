package com.dd.bot;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

	private static final String URL = "jdbc:sqlite:deadline-bot.db";

	private static final String CREATE_TABLE = """
			CREATE TABLE IF NOT EXISTS deadlines (
				id INTEGER PRIMARY KEY AUTOINCREMENT,
				guild_id TEXT NOT NULL,
				module TEXT NOT NULL,
				title TEXT NOT NULL,
				due_date TEXT NOT NULL,
				added_by TEXT NOT NULL,
				created_at TEXT DEFAULT CURRENT_TIMESTAMP
			)
			""";

	public DatabaseManager() throws SQLException {
		try (Connection conn = DriverManager.getConnection(URL); Statement stmt = conn.createStatement()) {
			stmt.execute(CREATE_TABLE);
		}
	}

	public void addDeadline(String guildId, String module, String title, LocalDate dueDate, String addedBy)
			throws SQLException {
		String sql = "INSERT INTO deadlines (guild_id, module, title, due_date, added_by)" + "VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(URL); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, guildId);
			ps.setString(2, module);
			ps.setString(3, title);
			ps.setString(4, dueDate.toString());
			ps.setString(5, addedBy);
			ps.executeUpdate();
		}
	}

	public List<Deadline> getUpcomingDeadlines(String guildId) throws SQLException {
		String sql2 = """
				SELECT id, module, title, due_date FROM deadlines
				WHERE due_date>=? AND guild_id = ?
				ORDER BY due_date
				""";

		List<Deadline> upcomingDeadlines = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(URL); PreparedStatement ps = conn.prepareStatement(sql2)) {
			ps.setString(1, LocalDate.now().toString());
			ps.setString(2, guildId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("id");
					String module = rs.getString("module");
					String title = rs.getString("title");
					String dueDateString = rs.getString("due_date");
					LocalDate dueDate = LocalDate.parse(dueDateString);

					Deadline deadline = new Deadline(id, module, title, dueDate);
					upcomingDeadlines.add(deadline);
				}
			}
		}
		return upcomingDeadlines;
	}
}
