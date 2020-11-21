package pl.morytko.moviemax.cinemas;

import java.util.List;
import java.util.Optional;

public interface CinemaService {
    List<Cinema> getCinemas();
    Optional<Cinema> getCinemaById(long id);
    void addCinema(Cinema cinema);
    void updateCinema(Cinema cinema);
    void deleteCinema(long id);
}
