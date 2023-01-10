package edu.school21.sockets.server;

import edu.school21.sockets.exceptions.IncorrectPasswordException;
import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.RoomsService;
import edu.school21.sockets.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class CommandHandler {

    private final SocketThread socketThread;
    private UsersService usersService;
    private RoomsService roomsService;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ROOM_NAME = "room name";

    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    @Autowired
    public void setRoomsService(RoomsService roomsService) {
        this.roomsService = roomsService;
    }

    public boolean authenticate() throws IOException {
        socketThread.send("1. SignUp\n2. SignIn\n3. Exit");
        String command = socketThread.readMessage();
        switch (command) {
            case "1":
                return signUp();
            case "2":
                return signIn();
            case "3":
                handleExit();
                return false;
            default:
                socketThread.send("Error: enter from 1 to 3");
                return authenticate();
        }
    }

    public boolean enterRoom() throws IOException {
        socketThread.send("1. Create room\n2. Choose room\n3. Exit");
        String command = socketThread.readMessage();
        switch (command) {
            case "1":
                return createRoom();
            case "2":
                return chooseRoom();
            case "3":
                handleExit();
                return false;
            default:
                socketThread.send("Error: enter 1-3");
                return enterRoom();
        }
    }

    private void handleExit() {
        socketThread.shutDownClient("You have left the chat.");
    }

    private boolean createRoom() throws IOException {
        String roomName = readValidField(ROOM_NAME);
        Optional<Room> createdRoom = roomsService.saveRoom(roomName);
        if (createdRoom.isPresent()) {
            socketThread.setRoom(createdRoom.get());
            return true;
        }
        socketThread.shutDownClient("Error: room with this name is already exist");
        return false;
    }

    private boolean chooseRoom() throws IOException {
        List<Room> rooms = roomsService.findAll();
        sendAllRooms(rooms);
        int cmdExit = rooms.size() + 1;
        while (true) {
            try {
                String str = socketThread.readMessage();
                int choose = Integer.parseInt(str);
                if (choose == cmdExit) {
                    handleExit();
                    return false;
                } else if (choose > 0 & choose < cmdExit) {
                    socketThread.setRoom(rooms.get(choose - 1));
                    return true;
                }
            } catch (NumberFormatException ignore) {
            }
            socketThread.send(format("Error: enter 1-%d", cmdExit));
        }
    }

    private void sendAllRooms(List<Room> rooms) {
        int menuCount = 1;
        StringBuilder sb = new StringBuilder();
        for (Room room : rooms) {
            sb.append(format("%d. %s\n", menuCount++, room.getName()));
        }
        sb.append(format("%d. Exit", menuCount));
        socketThread.send(sb.toString());
    }

    public boolean signIn() throws IOException {
        String username = readValidField(USERNAME);
        String password = readValidField(PASSWORD);
        try {
            Optional<User> user = usersService.signIn(username, password);
            if (user.isPresent()) {
                socketThread.setUser(user.get());
                return true;
            }
        } catch (IncorrectPasswordException ignore) {
        }
        socketThread.shutDownClient("Wrong username/password!");
        return false;
    }

    public boolean signUp() throws IOException {
        String username = readValidField(USERNAME);
        String password = readValidField(PASSWORD);
        Optional<User> createdUser = usersService.signUp(username, password);
        if (createdUser.isPresent()) {
            socketThread.send("Successful!");
            socketThread.setUser(createdUser.get());
            return true;
        }
        socketThread.shutDownClient("Error: user with this name is already exist");
        return false;
    }

    public String readValidField(String fieldName) throws IOException {
        socketThread.send(format("Enter %s:", fieldName));
        while (true) {
            String text = socketThread.readMessage();
            if (text.length() >= 2 && text.length() <= 30)
                return text;
            socketThread.send(format("Error: %s must contain from 2 to 30 chars", fieldName));
        }
    }

}
