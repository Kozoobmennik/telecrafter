package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.telegram.UserRoles;
import com.senderman.telecrafter.telegram.api.entity.Message;

public interface CommandExecutor {

    String getCommand();

    String getDescription();

    default UserRoles roleOnly() {
        return UserRoles.GUEST;
    }

    default boolean pmOnly() {
        return false;
    }

    void execute(Message message);

}
