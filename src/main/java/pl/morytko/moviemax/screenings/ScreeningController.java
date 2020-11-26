package pl.morytko.moviemax.screenings;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.morytko.moviemax.auditoriums.AuditoriumService;
import pl.morytko.moviemax.movies.MovieService;

@Controller
@RequestMapping("/screenings")
@AllArgsConstructor
public class ScreeningController {
    private final ScreeningService screeningService;
    private final AuditoriumService auditoriumService;
    private final MovieService movieService;

    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("screening", new Screening());
        model.addAttribute("auditoriums",auditoriumService.getAuditoriums());
        model.addAttribute("movies",movieService.getMovies());
        return "admin/screenings/screeningAddForm";
    }
}
