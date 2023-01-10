package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UsersService {
    Optional<User> signUp(String username, String password);
    Optional<User> signIn(String username, String password);
}
