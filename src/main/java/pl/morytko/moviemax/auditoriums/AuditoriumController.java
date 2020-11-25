package pl.morytko.moviemax.auditoriums;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.morytko.moviemax.cinemas.Cinema;
import pl.morytko.moviemax.cinemas.CinemaService;
import pl.morytko.moviemax.seats.Seat;
import pl.morytko.moviemax.seats.SeatService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        return "admin/auditoriums/auditoriumDetails";
    }

    @GetMapping("/add/{cinemaId}")
    public String showAddForm(@PathVariable long cinemaId, Model model){
        Optional<Cinema> cinema = cinemaService.getCinemaById(cinemaId);
        if (cinema.isPresent()){
            model.addAttribute("cinemaId",cinemaId);
            model.addAttribute("auditorium", new Auditorium());
            return "admin/auditoriums/auditoriumAddForm";
        }
        return "redirect:/cinemas/list";
    }

    @PostMapping("/add")
    public String addAuditorium(@RequestParam Map<String,String> allRequestParams){
        long cinemaId = Long.parseLong(allRequestParams.get("cinemaId"));
        int seatRowCount = Integer.parseInt(allRequestParams.get("seatRowCount"));
        int seatNumberCount = Integer.parseInt(allRequestParams.get("seatNumberCount"));
        int screenStart = 0;
        int screenEnd = 0;

        for (int i = 1; i <= seatNumberCount; i++) {
            int temp = Integer.parseInt(allRequestParams.get("screen"+i));
            if (temp==2){
                screenStart = i;
                break;
            }
        }
        for (int i = seatNumberCount; i >= 1; i--) {
            int temp = Integer.parseInt(allRequestParams.get("screen"+i));
            if (temp==2){
                screenEnd = i;
                break;
            }
        }

        Auditorium newAuditorium = new Auditorium();
        newAuditorium.setCinema(cinemaService.getCinemaById(cinemaId).get());
        newAuditorium.setScreenStart(screenStart);
        newAuditorium.setScreenEnd(screenEnd);
        newAuditorium.setSeatRowCount(seatRowCount);
        newAuditorium.setSeatNumberCount(seatNumberCount);
        Auditorium savedAuditorium = auditoriumService.addAuditorium(newAuditorium);

        for (int i = 1; i <= seatRowCount; i++) {
            for (int j = 1; j <= seatNumberCount; j++) {
                Seat newSeat = new Seat();
                short status = Short.parseShort(allRequestParams.get(i+"n"+j));
                newSeat.setRow(i);
                newSeat.setNumber(j);
                newSeat.setStatus(status);
                newSeat.setAuditorium(savedAuditorium);
                seatService.addSeat(newSeat);
            }
        }

        return "redirect:/cinemas/details/"+allRequestParams.get("cinemaId");
    }
}
