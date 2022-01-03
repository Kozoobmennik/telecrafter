package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.minecraft.PlayersInfo;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EarlyBirds implements CommandExecutor {

    public static final int LIMIT_MAX = 100;
    private final MinecraftProvider minecraft;
    private final TelegramProvider telegram;

    public EarlyBirds(TelegramProvider telegram, MinecraftProvider minecraft) {
        this.minecraft = minecraft;
        this.telegram = telegram;
    }

    @Override
    public String getCommand() {
        return "/earlybirds";
    }

    @Override
    public String getDescription() {
        return "топ сервера по пережитым рассветам";
    }

    @Override
    public void execute(Message message) {

        String[] params = getParams(message);

        int limit = 10;
        try {
            if (params.length >= 2) {
                limit = Math.min(Integer.parseInt(params[1]), LIMIT_MAX);
            }
        }
        catch (NumberFormatException ignored) {}

        PlayersInfo info = minecraft.getOnlineInfo();
        String top = Arrays.stream(info.getOfflinePlayers())
                .sorted((p1, p2) -> Integer.compare(getTickPlayer(p2), getTickPlayer(p1)))
                .limit(limit)
                .map(p -> "\uD83D\uDC64 " + p.getName() + " (" + formatTicks(p.getStatistic(Statistic.PLAY_ONE_MINUTE)) + ")")
                .collect(Collectors.joining("\n"));

        telegram.sendMessageToMainChat("<b>Топ ранних пташек:</b>\n\n" + top);
    }
    private String[] getParams(Message message) {
        return message.getText().split("\\s+", 2);
    }

    private int getTickPlayer(OfflinePlayer player) {
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE);
    }

    private String formatTicks(int ticks) {
        float dayTicks = 24000;
        float days = ticks/dayTicks;
        return String.format("%f game day(s)", days);
    }
}