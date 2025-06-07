package org.example;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BotWithDailyTask {

    public static void scheduleDailyTask(JDA jda) {
        String token = System.getenv("facts_api_token");
        Runnable task = () -> {
            // Replace with your channel ID
            TextChannel channel = jda.getTextChannelsByName("fun-stuff", true).getFirst();
            if (channel != null) {
                try (HttpClient client = HttpClient.newBuilder().build()) {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("https://api.api-ninjas.com/v1/facts"))
                            .header("X-Api-Key", token)
                            .build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    String json = response.body();

                    Pattern pattern = Pattern.compile("\"fact\"\\s*:\\s*\"(.*?)\"");
                    Matcher matcher = pattern.matcher(json);

                    if (matcher.find()) {
                        String fact = matcher.group(1);
                        channel.sendMessage("Good morning! ☀️ It's 7:00 AM!").queue();
                        channel.sendMessage("Here's a fun fact for you: " + fact).queue();
                        System.out.println("Fact: " + fact);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to fetch the fact: " + e.getMessage());
                    channel.sendMessage("Failed to fetch the fact.").queue();
                }
            }
        };

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Calculate delay until next 7:00 AM
        LocalTime targetTime = LocalTime.of(7, 0);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withHour(7).withMinute(0).withSecond(0).withNano(0);

        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusDays(1);
        }

        long initialDelay = Duration.between(now, nextRun).toMillis();
        long oneDay = Duration.ofDays(1).toMillis();

        scheduler.scheduleAtFixedRate(task, initialDelay, oneDay, TimeUnit.MILLISECONDS);
    }
}

