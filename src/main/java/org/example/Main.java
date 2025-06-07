package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.sql.*;

public class Main {
    public static void main(String[] arguments)
            throws LoginException, InterruptedException {
        WhoIsAlive.load();
        WhoIsAlive.find(null);
        // Uncomment the following lines to enable the Discord bot functionality
    }
//
//        String token = System.getenv("DISCORD_BOT_TOKEN");
//
//        if (token == null || token.isEmpty()) {
//            throw new IllegalStateException("DISCORD_BOT_TOKEN not set!");
//        }
//
//        JDA api = JDABuilder.createDefault(token)
//                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
//                .addEventListeners(new MyListener())
//                .build().awaitReady();
//
//        BotWithDailyTask.scheduleDailyTask(api);
}
