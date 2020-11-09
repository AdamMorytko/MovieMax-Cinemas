package pl.morytko.moviemax.home;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.morytko.moviemax.movies.MovieService;

@Controller
@AllArgsConstructor
public class HomeController {
    private MovieService movieService;

    @RequestMapping
    public String getHomePage(Model model){
        model.addAttribute("movies",movieService.getMovies());
        return "home";
    }
}
