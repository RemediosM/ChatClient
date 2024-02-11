package client.message;

import client.ChatClient;
import client.file.ClientDownloadFile;
import client.file.ClientSendFile;
import common.command.Command;
import javafx.util.Pair;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@AllArgsConstructor
public class MessageSender implements Runnable {

    private ChatClient chatClient;

    @Override
    public void run() {
        try {
            DataOutputStream outputStream = new DataOutputStream(chatClient.getSocket().getOutputStream());
            BufferedReader input;
            String message;
            while (true) {
                input = new BufferedReader(new InputStreamReader(System.in));
                message = input.readLine();

                if (Command.isCommand(message)) {
                    Pair<String, String> commandAndParam = Command.splitToCommandAndParam(message);
                    String command = commandAndParam.getKey();
                    String param = commandAndParam.getValue();
                    if (Command.SEND_FILE.equalsCommand(command)) {
                        new Thread(new ClientSendFile(param, chatClient.getRoomName())).start();
                    } else if (Command.DOWNLOAD_FILE.equalsCommand(command)) {
                        new Thread(new ClientDownloadFile(param, chatClient.getRoomName())).start();
                    } else {
                        outputStream.writeUTF(message);
                    }
                } else {
                    outputStream.writeUTF(message);
                }
                if (Command.LEAVE_CHAT.equalsCommand(message)) {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
