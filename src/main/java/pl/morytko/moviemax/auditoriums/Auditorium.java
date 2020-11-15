package pl.morytko.moviemax.auditoriums;

import lombok.Getter;
import lombok.Setter;
import pl.morytko.moviemax.cinemas.Cinema;
import pl.morytko.moviemax.seats.Seat;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Cinema cinema;
    @OneToMany(mappedBy = "auditorium")
    private List<Seat> seatList;

}
