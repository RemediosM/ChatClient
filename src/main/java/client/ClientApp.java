package client;

import common.configuration.Conf;
import common.message.SimpleMessage;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) {

        ChatClient chatClient = new ChatClient(Conf.HOST, Conf.CHAT_SERVER_PORT);
        chatClient.joinChat();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                SimpleMessage.directMessage(chatClient.getSocket(), "**q");
                chatClient.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

}


