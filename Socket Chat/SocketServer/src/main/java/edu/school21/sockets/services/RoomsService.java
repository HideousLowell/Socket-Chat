package edu.school21.sockets.services;

import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface RoomsService {
    Optional<Room> saveRoom(String name);

    List<Room> findAll();

    Optional<Room> findLastVisitedRoom(User user);

    void updateLastVisitedRoom(User user, Room room);
}
