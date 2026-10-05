# Discord Deadline Bot

A Discord bot that helps student groups keep track of upcoming deadlines, built in Java with JDA and SQLite.

Students can add deadlines with a slash command and pull up a sorted list of everything that's still due, all inside the Discord server they already use to talk with friends and coursemates on.

## Features (Current)
- `/adddeadline` - Adds a deadline with a module name, title and due date
- `/deadlines` - Lists all upcoming deadlines added in the server in order of due date (soonest first)
- `/ping` - Checks if the bot is online
- Some future planned features involve removing/editing deadlines incase of mistakes, automatic reminders (for example the bot pings @everyone or @here a certain amount of time before a deadline)

## Reason for building
- A mix of personal use and allowing friends on the same course as me to keep track of deadlines easily. Keeping track of deadlines especially on coursework with multiple people (Group Project Module) was a problem so this was an easy way for everyone to see deadlines.

## Input Validation
- Dates must be in the YYYY-MM-DD format. Anything else is rejected and an error message is displayed.
- Dates in the past are rejected
- Error messages can only be seen by the person who initiated the slash command so that the channel doesn't get cluttered

## Per-server data
- Deadlines are only linked to one server but the bot can be added to multiple servers
- This allows for wider use of the bot if needed

## Past deadlines
- Past deadlines are hidden when using /deadlines, so that the list stays relevant and isn't cluttered

## Running it yourself
- Go to Discord's Developer Portal and create a bot
- Get the token and Server ID
- Use DISCORD_TOKEN for your bot token and GUILD_ID for your server ID
- Import the project and run Bot.java
- Use commands
