package pl.morytko.moviemax.reservedSeats;

import lombok.Getter;
import lombok.Setter;
import pl.morytko.moviemax.reservations.Reservation;
import pl.morytko.moviemax.screenings.Screening;

import javax.persistence.*;
import java.util.Objects;

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
    @ManyToOne(fetch = FetchType.EAGER)
    private Screening screening;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservedSeat that = (ReservedSeat) o;
        return row == that.row &&
                number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, number);
    }
}
