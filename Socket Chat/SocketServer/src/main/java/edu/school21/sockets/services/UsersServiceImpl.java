package edu.school21.sockets.services;

import edu.school21.sockets.exceptions.IncorrectPasswordException;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> signUp(String username, String password) {
        if (usersRepository.findByName(username).isPresent())
            return Optional.empty();
        User user = new User(username, passwordEncoder.encode(password));
        usersRepository.save(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> signIn(String username, String password) {
        Optional<User> found = usersRepository.findByName(username);
        if (!found.isPresent()) return found;
        String realPassword = found.get().getPassword();
        if (!passwordEncoder.matches(password, realPassword))
            throw new IncorrectPasswordException();
        return found;
    }
}
