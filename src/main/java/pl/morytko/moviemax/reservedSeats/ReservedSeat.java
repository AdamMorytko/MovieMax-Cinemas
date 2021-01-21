package pl.morytko.moviemax.reservedSeats;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private long id;
    private int row;
    private int number;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Reservation reservation;


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
