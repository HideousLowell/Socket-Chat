package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomsRepository extends CrudRepository<Room> {
    Optional<Room> findByName(String name);

    Optional<Room> findLastVisitedRoom(User user);

    void saveLastVisitedRoom(User user, Room room);

    void updateLastVisitedRoom(User user, Room room);
}
