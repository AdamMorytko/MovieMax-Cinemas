package pl.morytko.moviemax.users;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

import static pl.morytko.moviemax.users.UserRole.*;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginForm(){
        return "main/login";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("user",new UserDto());
        return "main/registration";
    }

    @PostMapping("/registration")
    public String addNewUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "main/registration";
        }
        if (!userDto.getPassword().equals(userDto.getMatchingPassword())){
            bindingResult.rejectValue("matchingPassword","matchingPassword.incorrect"
                                ,"Hasła nie są identyczne.");
        }
        if (bindingResult.hasErrors()){
            return "main/registration";
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEnabled(true);
        Set<UserRole> roles = new HashSet<>();
        roles.add(USER);
        user.setRoles(roles);
        userService.addUser(user);
        model.addAttribute("newRegistration",true);
        return "redirect:/login";
    }
}
