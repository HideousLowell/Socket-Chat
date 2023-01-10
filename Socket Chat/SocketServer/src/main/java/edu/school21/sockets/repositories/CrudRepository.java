package edu.school21.sockets.repositories;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrudRepository<T> {

    Optional<T> findById(Long id);

    List<T> findAll();

    void save(T entity);

    void update(T entity);

    void delete(Long id);
}
