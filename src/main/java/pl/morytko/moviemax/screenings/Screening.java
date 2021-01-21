package pl.morytko.moviemax.screenings;

import lombok.Getter;
import lombok.Setter;
import pl.morytko.moviemax.auditoriums.Auditorium;
import pl.morytko.moviemax.movies.Movie;
import pl.morytko.moviemax.reservations.Reservation;
import pl.morytko.moviemax.reservedSeats.ReservedSeat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    private List<Reservation> reservationList;
}
