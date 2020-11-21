package pl.morytko.moviemax.auditoriums;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.morytko.moviemax.cinemas.CinemaService;
import pl.morytko.moviemax.seats.SeatService;

import java.util.Optional;

@Controller
@RequestMapping("/auditoriums")
@AllArgsConstructor
public class AuditoriumController {

    private final SeatService seatService;
    private final AuditoriumService auditoriumService;
    private final CinemaService cinemaService;

    @GetMapping("/details/{auditoriumId}")
    public String showAuditoriumDetails(@PathVariable long auditoriumId, Model model){
        Optional<Auditorium> auditorium = auditoriumService.getAuditoriumById(auditoriumId);
        if (auditorium.isPresent()){
            model.addAttribute("auditorium",auditorium.get());
            model.addAttribute("cinema",auditorium.get().getCinema());
            model.addAttribute("seats",seatService.getSeatsByAuditoriumId(auditoriumId));
            model.addAttribute("counter",new Counter());
        }
        return "admin/auditoriumDetails";
    }
}
