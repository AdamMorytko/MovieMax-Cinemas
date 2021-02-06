package pl.morytko.moviemax.users;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.morytko.moviemax.security.AppUserDetails;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserJpaService implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()){
            return new AppUserDetails(user.get());
        }else{
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }
}
