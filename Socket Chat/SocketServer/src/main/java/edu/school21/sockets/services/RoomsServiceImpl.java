package edu.school21.sockets.services;

import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.RoomsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomsServiceImpl implements RoomsService {

    private final RoomsRepository roomsRepository;

    @Override
    public Optional<Room> saveRoom(String name) {
        if (roomsRepository.findByName(name).isPresent())
            return Optional.empty();
        Room room = new Room(name);
        roomsRepository.save(room);
        return Optional.of(room);
    }

    @Override
    public List<Room> findAll() {
        return roomsRepository.findAll();
    }

    @Override
    public Optional<Room> findLastVisitedRoom(User user) {
        return roomsRepository.findLastVisitedRoom(user);
    }

    @Override
    public void updateLastVisitedRoom(User user, Room room) {
        Optional<Room> optRoom = roomsRepository.findLastVisitedRoom(user);
        if (optRoom.isPresent()) {
            roomsRepository.updateLastVisitedRoom(user, room);
        } else {
            roomsRepository.saveLastVisitedRoom(user, room);
        }
    }
}
