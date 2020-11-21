package pl.morytko.moviemax.auditoriums;

import java.util.List;
import java.util.Optional;

public interface AuditoriumService {
    List<Auditorium> getCinemaAuditoriums(long cinemaId);
    Optional<Auditorium> getAuditoriumOfCinemaById(long auditoriumId, long cinemaId);
    void addAuditorium(Auditorium auditorium);
    void updateAuditorium(Auditorium auditorium);
    void deleteAuditorium(long id);
    List<Long> getListOfAuditoriumIdOfCinema(long cinemaId);
}
