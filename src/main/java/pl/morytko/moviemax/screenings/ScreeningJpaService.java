package pl.morytko.moviemax.screenings;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    public void addScreening(Screening screening) {
        screeningRepository.save(screening);
    }
}
