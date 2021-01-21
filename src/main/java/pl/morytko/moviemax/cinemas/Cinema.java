package pl.morytko.moviemax.cinemas;

import lombok.Getter;
import lombok.Setter;
import pl.morytko.moviemax.auditoriums.Auditorium;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String city;
    @NotBlank
    private String street;
    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private List<Auditorium> auditoriumList;
}
