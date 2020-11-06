package pl.morytko.moviemax.movies;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@ToString
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 1, max = 100, message = "Tytuł musi mieć min 1 i max 100 znaków.")
    private String title;
    @NotBlank
    private String director;
    @NotBlank
    private String countryOrigin;
    @Size(min = 1, max = 600)
    private String description;
    @NotBlank
    private String genre;
    private int duration;
    @NotBlank
    private String posterUrl;

}