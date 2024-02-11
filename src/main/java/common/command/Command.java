package common.command;

import common.configuration.Conf;
import javafx.util.Pair;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Command {
    LEAVE_CHAT("q", "", "leave the chat", true),
    SEND_FILE("send_file", "filePath", "send file which path = filePath", true),
    DOWNLOAD_FILE("download_file", "fileName", "download file with name = fileName", true),

    CLIENT_UPDATE_ROOM_NAME("update_room_name", "roomName", "", false);

    @Getter
    private final String commandString;
    @Getter
    private final String paramName;
    @Getter
    private final String description;
    @Getter
    private final boolean isPublic;

    Command(String commandString, String paramName, String description, boolean isPublic) {
        this.commandString = Conf.COMMAND_IDENTIFIER + commandString;
        this.paramName = paramName;
        this.description = description;
        this.isPublic = isPublic;
    }

    public static boolean isCommand(String message) {
        return message.length() >= 2 && Conf.COMMAND_IDENTIFIER.equals(message.substring(0, 2));
    }

    public boolean equalsCommand(String command) {
        return this.getCommandString().equals(command);
    }

    public static Pair<String, String> splitToCommandAndParam(String message) {
        String command = "";
        String param = "";
        if (message.contains(" ")) {
            String[] commandParts = message.split(" ", 2);
            command = commandParts[0];
            param = commandParts[1];
        } else {
            command = message;
        }
        return new Pair<>(command, param);
    }

    @Override
    public String toString() {
        return commandString
                + (paramName.equals("") ? "" : " " + paramName + " ")
                + " - "
                + description;
    }

}
