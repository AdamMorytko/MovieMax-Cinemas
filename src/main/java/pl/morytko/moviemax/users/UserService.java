package pl.morytko.moviemax.users;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    void addUser(User user);
    void addUserList(List<User> users);
}
