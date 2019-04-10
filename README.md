# Telegram Storoj Bot

This bot is used in order to let your telegram chat members deside whether to allow a new member to join the chat.
Every new member is required to write a short text about himself, then his fate is desided by voting.

## System Requirements

To run this bot, you will need the following installed in your system:

* JRE 8 or higher
* Redis

To build the project from source, you will also need:

* Git
* Maven
* JDK 8 or higher
 		
## Building from source

1. Clone the repository using git: `git clone https://github.com/Sallatik/telegram-storoj-bot.git`
2. `cd telegram-storoj-bot` and `mvn install`

## Configuring

All config is done using bot.properties file. There are useful comments there explaining what each property is using for.
To run the bot in long-polling mode, you only have to specify `bot.token` and `bot.username` properties.
If you want to customize the messages, edit messages.properties file.

## Running

1. Run the redis server locally by executing `redis-server`
2. `cd` into project directory and execute `java -jar target/storoj-bot-jar-with-dependencies.jar`

## Usage

1. Add the bot to your group chat
2. Grant the bot with admin permissions to kick users and to invite users via link
3. Publish a link to your bot instead of an invite link, so that new users will know how to join your chat.

Note that the bot will kick anyone trying to sneak into the chat without permission
