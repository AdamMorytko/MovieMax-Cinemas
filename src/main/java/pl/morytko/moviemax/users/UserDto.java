package pl.morytko.moviemax.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
    @NotBlank
    private String matchingPassword;

    @NotBlank
    private String name;
    @NotBlank
    private String surname;
}
