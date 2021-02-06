package pl.morytko.moviemax.users;

import lombok.Getter;
import lombok.Setter;
import pl.morytko.moviemax.security.AppUserRole;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    @ElementCollection
    private Set<AppUserRole> roles;

}
