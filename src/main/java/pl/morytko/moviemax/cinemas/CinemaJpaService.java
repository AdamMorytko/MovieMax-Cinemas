package pl.morytko.moviemax.cinemas;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CinemaJpaService implements CinemaService{

    private final CinemaRepository cinemaRepository;


    @Override
    public List<Cinema> getCinemas() {
        return cinemaRepository.findAll();
    }

    @Override
    public Optional<Cinema> getCinemaById(long id) {
        return cinemaRepository.findById(id);
    }

    @Override
    public void addCinema(Cinema cinema) {
        cinemaRepository.save(cinema);
    }

    @Override
    public void addCinemas(List<Cinema> cinemaList) {
        cinemaRepository.saveAll(cinemaList);
    }

    @Override
    public void updateCinema(Cinema cinema) {
        cinemaRepository.save(cinema);
    }

    @Override
    public void deleteCinema(long id) {
        cinemaRepository.deleteById(id);
    }
}
