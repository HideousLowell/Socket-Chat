package edu.school21.sockets.mappers;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class MessageRowMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String text = rs.getString("text");
        LocalDateTime dateTime = rs.getObject("datetime", LocalDateTime.class);
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setName(rs.getString("username"));
        Room room = new Room();
        room.setId(rs.getLong("room_id"));
        room.setName(rs.getString("room_name"));
        return new Message(id, user, text, dateTime, room);
    }
}
