package pl.morytko.moviemax.cinemas;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.morytko.moviemax.auditoriums.AuditoriumService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/cinemas")
@AllArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;
    private final AuditoriumService auditoriumService;


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("cinema", new Cinema());
        return "admin/cinemas/cinemaAddForm";
    }

    @PostMapping("/add")
    public String addCinema(@Valid Cinema cinema, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/cinemas/cinemaAddForm";
        }
        cinemaService.addCinema(cinema);
        return "redirect:/cinemas/list";
    }

    @GetMapping("/details/{id}")
    public String showCinemaDetails(@PathVariable long id, Model model) {
        Optional<Cinema> cinemaOptional = cinemaService.getCinemaById(id);
        if (cinemaOptional.isPresent()) {
            model.addAttribute("cinema", cinemaOptional.get());
            model.addAttribute("auditoriumsIds",auditoriumService.getListOfAuditoriumIdOfCinema(id));
        }
        return "admin/cinemas/cinemaDetails";
    }

    @GetMapping("/list")
    public String showCinemasList(Model model) {
        model.addAttribute("cinemas", cinemaService.getCinemas());
        return "admin/cinemas/cinemaList";
    }
}
