package pl.morytko.moviemax.reservedSeats;

import lombok.Getter;
import lombok.Setter;
import pl.morytko.moviemax.reservations.Reservation;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ReservedSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int row;
    private int number;
    @ManyToOne(fetch = FetchType.EAGER)
    private Reservation reservation;
}
