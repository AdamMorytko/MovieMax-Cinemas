package pl.morytko.moviemax.users;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.morytko.moviemax.reservations.Reservation;
import pl.morytko.moviemax.reservations.ReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static pl.morytko.moviemax.users.UserRole.*;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final ReservationService reservationService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm() {
        return "main/user/login";
    }

    @GetMapping("/login/failure")
    public String showLoginFormAndError(Model model) {
        model.addAttribute("loginError", true);
        return "main/user/login";
    }

    @GetMapping("/login/success")
    public String showPrincipalPanel(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/admin";
        } else if (request.isUserInRole("ROLE_USER")) {
            return "redirect:/user";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "main/user/registration";
    }

    @PostMapping("/register")
    public String addNewUser(@ModelAttribute("user") @Validated({UserValidationGroups.UserData.class, UserValidationGroups.UserPassword.class}) UserDto userDto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "main/user/registration";
        }
        if (!userDto.getPassword().equals(userDto.getMatchingPassword())) {
            bindingResult.rejectValue("matchingPassword", "matchingPassword.notMatching"
                    , "Hasła nie są identyczne.");
        }
        if (!userService.checkIfUsernameAvailable(userDto.getUsername())){
            bindingResult.rejectValue("username", "username.alreadyExists"
                    , "Adres email już jest w użyciu.");
        }
        if (bindingResult.hasErrors()) {
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
        model.addAttribute("newRegistration", true);
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String showUserPanel(Principal principal, Model model) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        List<Reservation> reservations = user.getReservations();
        List<Reservation> threeLastReservations = reservations.stream()
                .sorted(Comparator.comparing(Reservation::getId).reversed())
                .limit(3)
                .collect(Collectors.toList());
        model.addAttribute("lastReservations", threeLastReservations);
        model.addAttribute("user", user);
        return "main/user/homePage";
    }

    @GetMapping("/user/edit")
    public String showEditUserDataForm(Principal principal, Model model) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setUsername(user.getUsername());
        model.addAttribute("user", userDto);
        return "main/user/userDataEdit";
    }

    @PostMapping("/user/edit")
    public String editUser(@ModelAttribute("user") @Validated(UserValidationGroups.UserData.class) UserDto userDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "main/user/userDataEdit";
        }
        User userToUpdate = (User) userService.loadUserByUsername(principal.getName());
        userToUpdate.setUsername(userDto.getUsername());
        userToUpdate.setName(userDto.getName());
        userToUpdate.setSurname(userDto.getSurname());
        userService.updateUser(userToUpdate);
        return "redirect:/user";
    }

    @GetMapping("/user/edit/password")
    public String showEditUserPasswordForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "main/user/userPasswordEdit";
    }

    @PostMapping("/user/edit/password")
    public String editUserPassword(@ModelAttribute("user") @Validated(UserValidationGroups.UserPassword.class) UserDto userDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "main/user/userPasswordEdit";
        }
        if (!userDto.getPassword().equals(userDto.getMatchingPassword())) {
            bindingResult.rejectValue("matchingPassword", "matchingPassword.notMatching"
                    , "Hasła nie są identyczne.");
        }
        if (bindingResult.hasErrors()) {
            return "main/user/userPasswordEdit";
        }
        User userToUpdate = (User) userService.loadUserByUsername(principal.getName());
        userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userService.updateUser(userToUpdate);
        return "redirect:/user";
    }

    @GetMapping("/user/reservation/{reservationId}")
    public String showUserReservationDetails(@PathVariable long reservationId, Model model, Principal principal){
        User user = (User) userService.loadUserByUsername(principal.getName());
        Optional<Reservation> reservationOptional = reservationService.getReservation(reservationId);
        Reservation reservation;
        if (reservationOptional.isPresent()){
            reservation = reservationOptional.get();
            if (!reservation.getUser().getUsername().equals(user.getUsername())){
                throw new IllegalArgumentException("Odmowa dostępu. Rezerwacja nie należy do zalogowanego użytkownika.");
            }
        }else{
            throw new IllegalArgumentException("Rezerwacja o podanym id nie istnieje.");
        }
        model.addAttribute("reservation",reservation);
        return "main/user/userReservationDetails";
    }

    @GetMapping("/user/reservations")
    public String showUserReservations(Model model, Principal principal){
        User user = (User) userService.loadUserByUsername(principal.getName());
        List<Reservation> reservations = user.getReservations();
        model.addAttribute("reservations",reservations);
        return "main/user/userReservations";
    }
}
