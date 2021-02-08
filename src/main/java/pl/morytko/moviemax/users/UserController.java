package pl.morytko.moviemax.users;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.morytko.moviemax.reservations.Reservation;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static pl.morytko.moviemax.users.UserRole.*;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm(){
        return "main/user/login";
    }

    @GetMapping("/login/failure")
    public String showLoginFormAndError(Model model){
        model.addAttribute("loginError",true);
        return "main/user/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user",new UserDto());
        return "main/user/registration";
    }

    @PostMapping("/register")
    public String addNewUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "main/user/registration";
        }
        if (!userDto.getPassword().equals(userDto.getMatchingPassword())){
            bindingResult.rejectValue("matchingPassword","matchingPassword.notMatching"
                                ,"Hasła nie są identyczne.");
        }
        if (bindingResult.hasErrors()){
            return "main/user/registration";
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEnabled(true);
        Set<UserRole> roles = new HashSet<>();
        roles.add(USER);
        user.setRoles(roles);
        userService.addUser(user);
        model.addAttribute("newRegistration",true);
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String showUserPanel(Principal principal, Model model){
        User user = (User) userService.loadUserByUsername(principal.getName());
        List<Reservation> reservations = user.getReservations();
        List<Reservation> threeLastReservations = reservations.stream()
                .sorted(Comparator.comparing(Reservation::getId).reversed())
                .limit(3)
                .collect(Collectors.toList());
        model.addAttribute("lastReservations",threeLastReservations);
        model.addAttribute("user",user);
        return "main/user/homePage";
    }
}
