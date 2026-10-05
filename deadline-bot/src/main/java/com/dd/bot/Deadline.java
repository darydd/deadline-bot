package com.dd.bot;

import java.time.LocalDate;

public record Deadline(int id, String module, String title, LocalDate dueDate) {}