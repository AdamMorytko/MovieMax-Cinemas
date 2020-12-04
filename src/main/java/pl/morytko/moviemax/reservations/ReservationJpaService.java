package pl.morytko.moviemax.reservations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ReservationJpaService implements ReservationService{
    private final ReservationRepository reservationRepository;

    @Override
    public void addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }
}
