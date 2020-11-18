package pl.morytko.moviemax.seats;

import java.util.List;

public interface SeatService {
    List<Seat> getSeatsByAuditoriumId(long auditoriumId);

}
