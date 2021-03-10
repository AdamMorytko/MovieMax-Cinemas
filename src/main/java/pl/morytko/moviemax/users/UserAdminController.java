package pl.morytko.moviemax.users;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.morytko.moviemax.reservations.ReservationService;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static pl.morytko.moviemax.users.UserRole.ADMIN;
import static pl.morytko.moviemax.users.UserRole.USER;

@Controller
@RequestMapping("/admin/users")
@AllArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @GetMapping("/list")
    public String showUserList(Model model){
        List<User> users = userService.getUsers();
        List<User> usersWithoutGuests = users.stream()
                .filter(user -> !user.getUsername().equals("guest@guest.guest"))
                .collect(Collectors.toList());
        model.addAttribute("users",usersWithoutGuests);
        return "admin/users/userList";
    }

    @GetMapping("/details/{userId}")
    public String showUserDetails(@PathVariable long userId, Model model){
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()){
            model.addAttribute("user",userOptional.get());
        }else{
            throw new IllegalArgumentException("Użytkownik o podanym id nie istnieje.");
        }
        return "admin/users/userDetails";
    }

    @GetMapping("/delete/{userId}")
    public String showDeleteConfirmation(@PathVariable long userId, Model model){
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()){
            model.addAttribute("user",userOptional.get());
        }else{
            throw new IllegalArgumentException("Użytkownik o podanym id nie istnieje.");
        }
        return "admin/users/userDeleteConfirmation";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam long userId, Principal principal){
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()){
            User loggedInUser = (User) userService.loadUserByUsername(principal.getName());
            User userToDelete = userOptional.get();
            if (loggedInUser.getUsername().equals(userToDelete.getUsername())){
                throw new IllegalArgumentException("Nie możesz usunąć swojego konta!");
            }
            userService.deleteUser(userToDelete);
        }else{
            throw new IllegalArgumentException("Użytkownik o podanym id nie istnieje.");
        }
        return "redirect:/admin/users/list";
    }

    @GetMapping("/edit/{userId}")
    public String showEditForm(@PathVariable long userId, Model model){
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()){
            User userToEdit = userOptional.get();
            UserDto userDto = new UserDto();
            userDto.setUsername(userToEdit.getUsername());
            userDto.setName(userToEdit.getName());
            userDto.setSurname(userToEdit.getSurname());
            model.addAttribute("user",userOptional.get());
        }else{
            throw new IllegalArgumentException("Użytkownik o podanym id nie istnieje.");
        }
        return "admin/users/userEditForm";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") @Validated(UserValidationGroups.UserData.class) UserDto userDto,
                           BindingResult bindingResult,
                           @RequestParam("userId") long userId){
        if (bindingResult.hasErrors()){
            return "admin/users/userEditForm";
        }
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()){
            User userToUpdate = userOptional.get();
            userToUpdate.setUsername(userDto.getUsername());
            userToUpdate.setName(userDto.getName());
            userToUpdate.setSurname(userDto.getSurname());
            userService.updateUser(userToUpdate);
        }else{
            throw new IllegalArgumentException("Użytkownik o podanym id nie istnieje.");
        }
        return "redirect:/admin/users/list";
    }

    @PostMapping("/role")
    public String changeUserRole(@RequestParam("userId") long userId){
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()){
            User userToUpdate = userOptional.get();
            String role = userToUpdate.getRoles().stream().findFirst().get().toString();
            Set<UserRole> roles = new HashSet<>();
            userToUpdate.setRoles(roles);
            if (role.equals("USER")){
                roles.add(ADMIN);
            }else if (role.equals("ADMIN")){
                roles.add(USER);
            }
            userToUpdate.setRoles(roles);
            userService.updateUser(userToUpdate);
        }else{
            throw new IllegalArgumentException("Użytkownik o podanym id nie istnieje.");
        }
        return "redirect:/admin/users/list";
    }

    @PostMapping("/enable")
    public String changeUserEnable(@RequestParam("userId") long userId){
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()){
            User userToUpdate = userOptional.get();
            userToUpdate.setEnabled(!userToUpdate.isEnabled());
            userService.updateUser(userToUpdate);
        }else{
            throw new IllegalArgumentException("Użytkownik o podanym id nie istnieje.");
        }
        return "redirect:/admin/users/list";
    }


}
