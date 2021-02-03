package pl.morytko.moviemax.users;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUsername(String username);
}
