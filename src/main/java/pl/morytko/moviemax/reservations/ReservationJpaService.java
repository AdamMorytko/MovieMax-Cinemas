package pl.morytko.moviemax.reservations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ReservationJpaService implements ReservationService{
    private final ReservationRepository reservationRepository;

    @Override
    public Reservation addReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public void addReservations(List<Reservation> reservationList) {
        reservationRepository.saveAll(reservationList);
    }

    @Override
    public void updateReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> getReservation(long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }
}
