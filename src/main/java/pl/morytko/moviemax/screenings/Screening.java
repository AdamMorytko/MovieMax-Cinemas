package pl.morytko.moviemax.screenings;

import lombok.Getter;
import lombok.Setter;
import pl.morytko.moviemax.auditoriums.Auditorium;
import pl.morytko.moviemax.movies.Movie;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Auditorium auditorium;
    @ManyToOne(fetch = FetchType.EAGER)
    private Movie movie;
    private LocalDate screeningDate;
    private LocalTime screeningTime;

}
