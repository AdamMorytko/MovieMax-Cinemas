package pl.morytko.moviemax.cinemas;

import java.util.List;

public interface CinemaService {
    List<Cinema> getCinemas();
    void addCinema(Cinema cinema);
    void updateCinema(Cinema cinema);
    void deleteCinema(long id);
}
