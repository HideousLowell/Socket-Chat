package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomsRepositoryImpl implements RoomsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Room> rowMapper = new BeanPropertyRowMapper<>(Room.class);

    @Override
    public Optional<Room> findById(Long id) {
        return jdbcTemplate
                .query("SELECT * FROM rooms WHERE id = ?", rowMapper, id)
                .stream().findFirst();
    }

    @Override
    public List<Room> findAll() {
        return jdbcTemplate.query("SELECT * FROM rooms", rowMapper);
    }

    @Override
    public void save(Room entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO rooms (name) VALUES (?)", new String[]{"id"});
            ps.setString(1, entity.getName());
            return ps;
        }, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Room entity) {
        jdbcTemplate.update("UPDATE rooms SET name = ? WHERE id = ?",
                entity.getName(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM rooms WHERE id = ?", id);
    }

    @Override
    public Optional<Room> findByName(String name) {
        return jdbcTemplate
                .query("SELECT * FROM rooms WHERE name = ?", rowMapper, name)
                .stream().findFirst();
    }

    @Override
    public Optional<Room> findLastVisitedRoom(User user) {
        return jdbcTemplate
                .query("SELECT r.* FROM rooms r " +
                        "JOIN last_visited_rooms lvr on r.id = lvr.room_id " +
                        "WHERE user_id = ?", rowMapper, user.getId())
                .stream().findFirst();
    }
    @Override
    public void saveLastVisitedRoom(User user, Room room) {
        jdbcTemplate.update("INSERT INTO  last_visited_rooms VALUES (? , ?)",
                user.getId(), room.getId());
    }
    @Override
    public void updateLastVisitedRoom(User user, Room room) {
        jdbcTemplate.update("UPDATE last_visited_rooms SET room_id = ?" +
                " WHERE user_id = ?", room.getId(), user.getId());
    }

}
