package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
public class UsersRepositoryImpl implements UsersRepository {

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);

    @Override
    public Optional<User> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM users u WHERE id = ?",
                        rowMapper, id)
                .stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users ", rowMapper);
    }

    @Override
    public void save(User entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String SQL_INSERT = "INSERT INTO users (name, password) VALUES (?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, new String[]{"id"});
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getPassword());
            return ps;
        }, keyHolder);
        entity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update("UPDATE users SET name = ?, password = ? WHERE id = ?",
                entity.getName(), entity.getPassword(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    @Override
    public Optional<User> findByName(String name) {
        return jdbcTemplate.query("SELECT * FROM users u WHERE name = ?",
                        rowMapper, name)
                .stream().findFirst();
    }
}
