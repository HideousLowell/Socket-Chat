package edu.school21.sockets.server;

import edu.school21.sockets.json.JsonConverter;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.MessagesService;
import edu.school21.sockets.services.RoomsService;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

@Component
@Scope("prototype")
@Setter
@Getter
public class SocketThread extends Thread {

    private final String DISCONNECT = "21";

    private final Socket socket;
    private Server server;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private MessagesService messagesService;
    private ApplicationContext app;
    private RoomsService roomsService;

    private CommandHandler commandHandler;
    private User user;
    private Room room;

    public SocketThread(Socket socket, ApplicationContext app) {
        this.socket = socket;
        this.app = app;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            commandHandler = app.getBean(CommandHandler.class, this);
        } catch (IOException ignore) {
            return;
        }
        start();
    }

    @Autowired
    public void setServer(Server server) {
        this.server = server;
    }

    @Autowired
    public void setMessagesService(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Autowired
    public void setRoomsService(RoomsService roomsService) {
        this.roomsService = roomsService;
    }

    @Override
    public void run() {
        try {
            send("Hello from Server!");
            if (!commandHandler.authenticate()) return;
            if (!commandHandler.enterRoom()) return;
            mainChatLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mainChatLoop() throws IOException {
        send(room.getName() + " --- ");
        sendLast30Messages();
        String message = readMessage();
        while (!message.equals("Exit")) {
            messagesService.saveMessage(user, message, room);
            server.sendUserMessageToAll(user, message, room);
            message = readMessage();
        }
        shutDownClient("You have left the chat.");
    }

    private void sendLast30Messages() {
        if (!lastRoomControl()) return;
        List<Message> messages = messagesService.getLastRoomMessages(room, 30);
        messages.stream()
                .map(JsonConverter::convert)
                .forEach(this::sendJson);
    }

    private boolean lastRoomControl() {
        Optional<Room> optionalRoom = roomsService.findLastVisitedRoom(user);
        boolean result = false;
        if (optionalRoom.isPresent()) {
            Room lastRoom = optionalRoom.get();
            result = room.equals(lastRoom);
        }
        if (!result)
            roomsService.updateLastVisitedRoom(user, room);
        return result;
    }

    String readMessage() throws IOException {
        try {
            JSONObject obj = (JSONObject) in.readObject();
            return (String) obj.get("message");
        } catch (ClassNotFoundException e) {
            shutDownClient("Unknown format");
        }
        return "";
    }

    public void send(String msg) {
        sendJson(JsonConverter.convert("message", msg));
    }

    public void sendJson(JSONObject jsonObject) {
        try {
            out.writeObject(jsonObject);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void shutDownClient(String message) {
        send(message);
        send(DISCONNECT);
    }

    @PreDestroy
    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
