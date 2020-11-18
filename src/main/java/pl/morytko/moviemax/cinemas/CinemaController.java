package pl.morytko.moviemax.cinemas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("cinema", new Cinema());
        return "admin/cinemaAddForm";
    }

    @GetMapping("/details/{id}")
    public String showCinemaDetails(@PathVariable long id, Model model){
        model.addAttribute("cinema", cinemaService.getMovieById(id).get());
        return "admin/cinemaDetails";
    }

    @PostMapping("/add")
    public String addCinema(@Valid Cinema cinema, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
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
