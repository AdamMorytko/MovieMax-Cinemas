package pl.morytko.moviemax.reservations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.morytko.moviemax.auditoriums.AuditoriumService;
import pl.morytko.moviemax.reservedSeats.ReservedSeatService;
import pl.morytko.moviemax.screenings.ScreeningService;

@Controller
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservedSeatService reservedSeatService;
    private final ScreeningService screeningService;
    private final AuditoriumService auditoriumService;

    @GetMapping
    public String showReservationForm(Model model){

        return "main/reservations/reservationAddForm";
    }
}
