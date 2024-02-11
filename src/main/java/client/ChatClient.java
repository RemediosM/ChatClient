package client;

import client.message.MessageReceiver;
import client.message.MessageSender;
import common.configuration.Conf;
import common.logger.Logg;
import lombok.Getter;

import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    private String host;
    private int port;
    @Getter
    private Socket socket;
    private String roomName = Conf.MAIN_ROOM_NAME;
    private final Object lock = new Object();

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getRoomName() {
        synchronized (lock) {
            return roomName;
        }
    }

    public void setRoomName(String roomName) {
        synchronized (lock) {
            this.roomName = roomName;
        }
    }

    public void joinChat() {
        try {
            socket = new Socket(host, port);
            MessageReceiver messageReceiver = new MessageReceiver(this);
            new Thread(messageReceiver).start();
            MessageSender messageSender = new MessageSender(this);
            new Thread(messageSender).start();
        } catch (IOException e) {
            Logg.error("Cannot connect with the server.");
        }
    }

}
