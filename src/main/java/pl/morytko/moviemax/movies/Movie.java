package pl.morytko.moviemax.movies;

import lombok.Getter;
import lombok.Setter;
import pl.morytko.moviemax.screenings.Screening;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
public class Movie implements Comparable<Movie>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 1, max = 100, message = "Tytuł musi mieć min 1 i max 100 znaków.")
    private String title;
    @NotBlank(message = "To pole nie może byc puste!")
    private String director;
    @NotBlank(message = "To pole nie może byc puste!")
    private String countryOrigin;
    @Size(max = 600, message = "Opis może mieć maksymalnie 600 znaków.")
    private String description;
    @NotBlank(message = "To pole nie może byc puste!")
    private String genre;
    @NotNull(message = "To pole nie może byc puste!")
    @Min(value = 1,message = "Czas trwania musi być większy od 0!")
    private int duration;
    @NotBlank(message = "To pole nie może byc puste!")
    private String posterUrl;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Screening> screeningList;

    @Override
    public int compareTo(Movie o) {
        int last = this.title.compareTo(o.title);
        return last == 0 ? this.title.compareTo(o.title) : last;
    }
}