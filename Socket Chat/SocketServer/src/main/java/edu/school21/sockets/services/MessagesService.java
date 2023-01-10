package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface MessagesService {
    Optional<Message> saveMessage(User user, String text, Room room);

    List<Message> getLastRoomMessages(Room room, int number);
}
