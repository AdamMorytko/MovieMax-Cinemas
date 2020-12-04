package pl.morytko.moviemax.screenings;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ScreeningJpaService implements ScreeningService{
    private final ScreeningRepository screeningRepository;

    @Override
    public List<Screening> getScreeningsByAuditoriumId(long auditoriumId) {
        return screeningRepository.findAllByAuditoriumId(auditoriumId);
    }

    @Override
    public List<Screening> getScreeningsByCinemaAndDate(long cinemaId, LocalDate screeningDate) {
        return screeningRepository.findAllByAuditorium_Cinema_IdAndScreeningDate(cinemaId,screeningDate);
    }

    @Override
    public void addScreening(Screening screening) {
        screeningRepository.save(screening);
    }

    @Override
    public Optional<Screening> getScreeningById(long id) {
        return screeningRepository.findById(id);
    }
}
