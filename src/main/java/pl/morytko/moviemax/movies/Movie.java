package pl.morytko.moviemax.movies;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.morytko.moviemax.screenings.Screening;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Movie implements Comparable<Movie>{
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
    @OneToMany(mappedBy = "movie")
    private List<Screening> screeningList;

    @Override
    public int compareTo(Movie o) {
        int last = this.title.compareTo(o.title);
        return last == 0 ? this.title.compareTo(o.title) : last;
    }
}