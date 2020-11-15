package pl.morytko.moviemax.seats;

import lombok.Getter;
import lombok.Setter;
import pl.morytko.moviemax.auditoriums.Auditorium;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Auditorium auditorium;

    private int row;
    private int number;
}
