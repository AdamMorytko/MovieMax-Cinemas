package pl.morytko.moviemax.reservations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.morytko.moviemax.auditoriums.Auditorium;
import pl.morytko.moviemax.auditoriums.AuditoriumService;
import pl.morytko.moviemax.reservedSeats.ReservedSeatService;
import pl.morytko.moviemax.screenings.Screening;
import pl.morytko.moviemax.screenings.ScreeningService;
import pl.morytko.moviemax.seats.SeatService;
import pl.morytko.moviemax.utils.Counter;
import pl.morytko.moviemax.utils.ReservedSeatUtil;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservedSeatService reservedSeatService;
    private final ScreeningService screeningService;
    private final AuditoriumService auditoriumService;
    private final SeatService seatService;

    @PostMapping("/new")
    public String showFirstForm(@RequestParam("screeningId") String screeningIdParam, Model model) {
        long screeningId = 0;
        if (screeningIdParam.isEmpty()) {
            return "redirect:/";
        } else {
            screeningId = Long.parseLong(screeningIdParam);
        }
        Optional<Screening> screening = screeningService.getScreeningById(screeningId);
        if (screening.isPresent()) {
            model.addAttribute("screeningId", screeningId);
            return "main/reservations/chooseSeatNumberForm";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/second")
    public String showSecondForm(@RequestParam Map<String, String> allRequestParams, Model model) {
        String screeningIdParam = allRequestParams.get("screeningId");
        String reservedSeatNumberParam = allRequestParams.get("reservedSeatNumber");
        long screeningId = 0;
        int reservedSeatNumber = 0;
        if (screeningIdParam.isEmpty() || reservedSeatNumberParam.isEmpty()) {
            return "redirect:/";
        } else {
            screeningId = Long.parseLong(screeningIdParam);
            reservedSeatNumber = Integer.parseInt(reservedSeatNumberParam);
        }
        Optional<Screening> screening = screeningService.getScreeningById(screeningId);
        if (screening.isPresent() && reservedSeatNumber > 0) {
            model.addAttribute("screeningId", screeningId);
            model.addAttribute("reservedSeatNumber", reservedSeatNumber);
            Optional<Auditorium> auditorium = auditoriumService.getAuditoriumById(screening.get().getAuditorium().getId());
            if (auditorium.isPresent()) {
                model.addAttribute("auditorium", auditorium.get());
                model.addAttribute("cinema", auditorium.get().getCinema());
                model.addAttribute("seats", seatService.getSeatsByAuditoriumId(auditorium.get().getId()));
                model.addAttribute("reservedSeats",reservedSeatService.getReservedSeatsByScreeningId(screeningId));
                model.addAttribute("counter", new Counter());
                model.addAttribute("rsUtil",new ReservedSeatUtil());
            }else{
                return "redirect:/";
            }
            return "main/reservations/chooseSeatsForm";
        } else {
            return "redirect:/";
        }
    }

}
