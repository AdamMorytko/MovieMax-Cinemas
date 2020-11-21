package pl.morytko.moviemax.seats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import pl.morytko.moviemax.auditoriums.Auditorium;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Auditorium auditorium;

    @NotNull
    private int row;
    @NotNull
    private int number;
    @NotNull
    private short status;
}
