package edu.school21.sockets.server;

import edu.school21.sockets.json.JsonConverter;
import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.UsersService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Getter
public class Server {

    private final List<SocketThread> socketThreads = new LinkedList<>();
    private final UsersService usersService;
    private final ApplicationContext app;

    public void exec(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("The server is running on port:%d\n", port);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    socketThreads.add(app.getBean(SocketThread.class, socket, app));
                } catch (IOException ignore) {
                    System.out.println("Failed connection attempt");
                }
            }
        } catch (IOException e) {
            System.err.println("Connection refused");
        }
        System.out.println("The server is stopped!");
    }

    public void sendUserMessageToAll(User user, String message, Room room) {
        socketThreads.removeIf(th -> !th.isAlive());
        JSONObject jsonObject = JsonConverter.convert(user, message);
        socketThreads.stream()
                .filter(st -> room.equals(st.getRoom()))
                .forEach(th -> th.sendJson(jsonObject));
    }
}
