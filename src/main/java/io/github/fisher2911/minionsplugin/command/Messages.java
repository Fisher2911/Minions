package io.github.fisher2911.minionsplugin.command;

import io.github.fisher2911.fishcore.message.Message;
import io.github.fisher2911.fishcore.message.Placeholder;

public class Messages {

    public static final Message MUST_BE_PLAYER = new Message(
            "command-must-be-player",
            "<red>You must be a player to execute this command!"
    );

    public static final Message MUST_BE_CONSOLE = new Message(
            "command-must-be-console",
            "<red>This command can only be executed from the console!"
    );

    public static final Message REQUIRES_ID = new Message(
            "command-requires-id",
            "<red>You must specify an id!"
    );

    public static final Message INVALID_ID = new Message(
            "command-invalid-id",
            "<red>That id is invalid!"
    );

    public static final Message ERROR_RECEIVING_ITEM = new Message(
            "command-error-receiving-item",
            "<red>There was an error receiving that item"
    );

    public static final Message GIVEN_ITEM = new Message(
            "command-given-item",
            "<red>You were given " + Placeholder.ITEM
    );

}
