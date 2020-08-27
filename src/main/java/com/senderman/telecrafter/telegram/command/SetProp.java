package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public class SetProp implements CommandExecutor {

    private final TelecrafterBot telegram;
    private final ServerPropertiesProvider serverProperties;

    public SetProp(TelecrafterBot telegram, ServerPropertiesProvider serverProperties) {
        this.telegram = telegram;
        this.serverProperties = serverProperties;
    }

    @Override
    public String getCommand() {
        return "/setprop";
    }

    @Override
    public boolean adminsOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        String[] params = message.getText().split("\\s+", 3);
        if (params.length < 3) {
            telegram.sendMessage("Неверный формат! " + getCommand() + " key value");
            return;
        }
        if (serverProperties.setProperty(params[1], params[2]))
            telegram.sendMessage("Новое значение для " + params[1] + " установлено!");
        else
            telegram.sendMessage("Такого ключа нет!");
    }
}