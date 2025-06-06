package org.example;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Timer;

public class MyListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (event.isFromGuild()) {
            String guildName = event.getGuild().getName();
            long guildId = event.getGuild().getIdLong();
            String channelName = event.getChannel().getName();
            String authorName = event.getAuthor().getName();
            String content = event.getMessage().getContentRaw();

            System.out.println("Message from server: " + guildName + " (ID: " + guildId + ")");
            System.out.println("Channel: " + channelName);
            System.out.println("Author: " + authorName);
            System.out.println("Content: " + content);
            if (content.startsWith("!countdown")) {
                doingCountDown(event, content);
            } else if (content.startsWith("!sort")) {
                String[] parts = content.split(" "); // "!sort bubble 1000"
                if (parts.length < 3) {
                    event.getChannel().sendMessage("Please provide a sorting type or size in this format: !sort bubble [size]").queue();
                    return;
                }

                String sortingTypeStr = parts[1].trim().toUpperCase(); // BUBBLE
                int size = Integer.parseInt(parts[2]);

                if (size <= 0 || size > 400) {
                    event.getChannel().sendMessage("Size must be between 1 and 400.").queue();
                    return;
                }

                Sorting.SortingType type = Sorting.SortingType.valueOf(sortingTypeStr);

                Sorting sorter = new Sorting.Builder()
                        .setSize(size)
                        .setSortingType(type)
                        .build();

                int[] sorted = sorter.sort();

                event.getChannel().sendMessage("Sorted: " + Arrays.toString(sorted)).queue();
            }
        } else {
            System.out.println("Message from a private DM: " + event.getMessage().getContentRaw());
        }
    }

    public void doingCountDown(MessageReceivedEvent event, String content) {
        MessageChannel channel = event.getChannel();
        channel.sendMessage("Countdown started!").queue();
//        int time = Integer.parseInt(content.split(" ")[1]);
        int time = 10; // Default countdown time
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            int remainingTime = time;

            @Override
            public void run() {
                if (remainingTime > 0) {
                    channel.sendMessage("Time left: " + remainingTime + " seconds").queue();
                    remainingTime--;
                } else {
                    channel.sendMessage("Countdown finished!").queue();
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        var member = event.getMember();
        var user = member.getUser();
        Guild guild = event.getGuild();
        TextChannel channel = guild.getTextChannelsByName("testing", true).getFirst();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter);

        if (event.getChannelJoined() != null) {
            String message = user.getAsTag() + " joined voice channel: " + event.getChannelJoined().getName()
                    + " | Time: " + formattedTime;
            System.out.println(message);
            channel.sendMessage(message).queue();
        }

        if (event.getChannelLeft() != null) {
            String message = user.getAsTag() + " left voice channel: " + event.getChannelLeft().getName()
                    + " | Time: " + formattedTime;
            System.out.println(message);
            channel.sendMessage(message).queue();
        }
    }
}
