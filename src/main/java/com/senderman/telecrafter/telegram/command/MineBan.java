package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.UserRoles;
import com.senderman.telecrafter.telegram.api.entity.Message;
import org.bukkit.command.CommandException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MineBan implements CommandExecutor {

    private final TelegramProvider telegram;
    private final MinecraftProvider minecraft;

    public MineBan(TelegramProvider telegram, MinecraftProvider minecraft) {
        this.telegram = telegram;
        this.minecraft = minecraft;
    }

    @Override
    public String getCommand() {
        return "/ban";
    }

    @Override
    public String getDescription() {
        return "забанить игрока";
    }

    @Override
    public UserRoles roleOnly() {
        return UserRoles.MODERATOR;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();

        String[] args = message.getText().split("\\s+", 2);
        if (args.length < 2) {
            telegram.sendMessage(chatId, "Использование: " + getCommand() + " команда");
            return;
        }
        if (args[1].length() > 25) {
            telegram.sendMessage(chatId, "Слишком длинный ник");
            return;
        }

        String command = String.format(
                "%s %s",
                "ban",
                args[1].replaceAll(" ", "")
        );

        try {
            minecraft.runCommand(
                    command,
                    successful -> telegram.sendMessage(
                            chatId,
                            successful ? "Игрок забанен!" : "Что-то пошло не так."
                    )
            );
        } catch (CommandException e) {
            telegram.sendMessage(chatId, "<b>Ошибка выполнения команды!</b>\n\n" + exceptionToString(e));
        }
    }

    private String exceptionToString(Exception e) {
        try (
                StringWriter string = new StringWriter();
                PrintWriter pw = new PrintWriter(string)
        ) {
            e.printStackTrace(pw);
            return string.toString();
        } catch (IOException ioException) {
            throw new IllegalStateException(ioException);
        }
    }
}
