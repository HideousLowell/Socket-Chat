package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.MessagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessagesServiceImpl implements MessagesService {

    private final MessagesRepository messagesRepository;

    @Override
    public Optional<Message> saveMessage(User user, String text, Room room) {
        Message message = new Message(user, text, room);
        messagesRepository.save(message);
        return Optional.of(message);
    }

    @Override
    public List<Message> getLastRoomMessages(Room room, int number) {
        return messagesRepository.findLastRoomMessages(room, number);
    }
}
