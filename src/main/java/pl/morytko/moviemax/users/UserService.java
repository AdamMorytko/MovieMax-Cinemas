package pl.morytko.moviemax.users;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    void addUser(User user);
    boolean checkIfUsernameAvailable(String username);
    void addUserList(List<User> users);
    void updateUser(User user);
    void deleteUser(User user);
    List<User> getUsers();
    Optional<User> getUserById(long userId);
}
