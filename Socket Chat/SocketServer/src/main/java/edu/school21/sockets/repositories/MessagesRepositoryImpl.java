package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.mappers.MessageRowMapper;
import edu.school21.sockets.models.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MessagesRepositoryImpl implements MessagesRepository {

    private final MessageRowMapper rowMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Message> findById(Long id) {
        return jdbcTemplate.query(
                "SELECT m.*, u.name as username, r.name as roomname FROM messages m " +
                        "JOIN users u on u.id = m.user_id " +
                        "JOIN rooms r on r.id = m.room_id " +
                        "WHERE m.id = ?",
                rowMapper, id).stream().findFirst();
    }

    @Override
    public List<Message> findAll() {
        return jdbcTemplate.query(
                "SELECT m.*, u.name as username, r.name as roomname FROM messages m " +
                        "JOIN users u on u.id = m.user_id " +
                        "JOIN rooms r on r.id = m.room_id",
                rowMapper);
    }

    @Override
    public void save(Message entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String SQL_INSERT = "INSERT INTO messages (user_id, text, datetime, room_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, new String[]{"id"});
            ps.setLong(1, entity.getUser().getId());
            ps.setString(2, entity.getText());
            ps.setObject(3, entity.getDateTime());
            ps.setLong(4, entity.getRoom().getId());
            return ps;
        }, keyHolder);
        entity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public void update(Message entity) {
        jdbcTemplate.update("UPDATE messages SET user_id = ?, text = ?, datetime = ?, room_id = ? WHERE id = ?",
                entity.getUser().getId(), entity.getText(), entity.getDateTime(), entity.getRoom().getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM messages WHERE id = ?", id);
    }

    @Override
    public List<Message> findLastRoomMessages(Room room, int number) {
        return jdbcTemplate.query(
                "SELECT m.*, u.name as username, r.name as room_name FROM messages m " +
                        "JOIN users u on u.id = m.user_id JOIN rooms r on r.id = m.room_id " +
                        "WHERE room_id = ? ORDER BY datetime DESC LIMIT ?",
                rowMapper, room.getId(), number);
    }
}
