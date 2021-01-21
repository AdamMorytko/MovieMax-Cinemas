package pl.morytko.moviemax.reservations;

import lombok.Getter;
import lombok.Setter;
import pl.morytko.moviemax.reservedSeats.ReservedSeat;
import pl.morytko.moviemax.screenings.Screening;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Screening screening;
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservedSeat> reservedSeats;
    private int reservedSeatNumber;
    private String userName;
    private String userSurname;
    private String email;
}
