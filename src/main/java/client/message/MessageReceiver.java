package client.message;

import client.ChatClient;
import common.command.Command;
import javafx.util.Pair;
import lombok.AllArgsConstructor;

import java.io.DataInputStream;
import java.io.IOException;

@AllArgsConstructor
public class MessageReceiver implements Runnable {

    private ChatClient chatClient;

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(chatClient.getSocket().getInputStream());
            while (!chatClient.getSocket().isClosed()) {
                String message = inputStream.readUTF();
                if (Command.isCommand(message)) {
                    Pair<String, String> commandAndParam = Command.splitToCommandAndParam(message);
                    String command = commandAndParam.getKey();
                    String param = commandAndParam.getValue();
                    if (Command.CLIENT_UPDATE_ROOM_NAME.equalsCommand(command)) {
                        chatClient.setRoomName(param);
                    }
                } else {
                    System.out.println(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
