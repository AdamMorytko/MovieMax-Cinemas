package pl.morytko.moviemax.users;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.morytko.moviemax.reservations.ReservationService;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class UserAdminController {
    private final UserService userService;
    private final ReservationService reservationService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/admin/users/list")
    public String showUserList(Model model){
        List<User> users = userService.getUsers();
        model.addAttribute("users",users);
        return "admin/users/userList";
    }
}
