package pl.morytko.moviemax.cinemas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.morytko.moviemax.auditoriums.Auditorium;
import pl.morytko.moviemax.auditoriums.AuditoriumService;
import pl.morytko.moviemax.seats.Seat;
import pl.morytko.moviemax.seats.SeatService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cinemas")
@AllArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;
    private final AuditoriumService auditoriumService;
    private final SeatService seatService;


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("cinema", new Cinema());
        return "admin/cinemaAddForm";
    }

    @GetMapping("/details/{id}")
    public String showCinemaDetails(@PathVariable long id, Model model) {
        Optional<Cinema> cinemaOptional = cinemaService.getMovieById(id);
        if (cinemaOptional.isPresent()) {
            ObjectMapper mapper = new ObjectMapper();
            model.addAttribute("cinema", cinemaOptional.get());
            List<Auditorium> cinemaAuditoriums = auditoriumService.getCinemaAuditoriums(id);
            model.addAttribute("auditoriums", cinemaAuditoriums);
            List<List<Seat>> arrayListList = new ArrayList<>();
            cinemaAuditoriums.forEach(a ->
                    arrayListList.add(seatService.getSeatsByAuditoriumId(a.getId())));
            try {
                model.addAttribute("allAuditoriumsSeats",mapper.writeValueAsString(arrayListList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            model.addAttribute("auditoriumsCount",cinemaAuditoriums.size());
        }

        return "admin/cinemaDetails";
    }

    @PostMapping("/add")
    public String addCinema(@Valid Cinema cinema, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/cinemaAddForm";
        }
        cinemaService.addCinema(cinema);
        return "redirect:/cinemas/list";
    }

    @GetMapping("/list")
    public String showCinemasList(Model model) {
        model.addAttribute("cinemas", cinemaService.getCinemas());
        return "admin/cinemaList";
    }
}
