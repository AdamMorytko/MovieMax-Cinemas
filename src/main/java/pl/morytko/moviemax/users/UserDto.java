package pl.morytko.moviemax.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDto {

    @NotBlank(groups = UserValidationGroups.UserData.class)
    @Email(groups = UserValidationGroups.UserData.class)
    private String username;
    @NotBlank(groups = UserValidationGroups.UserData.class)
    private String name;
    @NotBlank(groups = UserValidationGroups.UserData.class)
    private String surname;

    @NotBlank(groups = UserValidationGroups.UserPassword.class)
    private String password;
    @NotBlank(groups = UserValidationGroups.UserPassword.class)
    private String matchingPassword;


}
