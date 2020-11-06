package pl.morytko.moviemax.movies;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("movie",new Movie());
        return "/movies/addForm";
    }

    @PostMapping("/add")
    public String addMovie(@Valid Movie movie, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "/movies/addForm";
        }
        movieService.addMovie(movie);
        return "redirect:/movies";
    }

    @RequestMapping("/filmweb")
    public String fillAddForm(@RequestParam String filmwebUrl, Model model) {
        Movie movie = new Movie();
        Filmweb filmweb;
        try {
            filmweb = new Filmweb(filmwebUrl);
        }catch (IOException ioException){
            model.addAttribute("errorLoadingPage","true");
            return "movies/addForm";
        }
        movie.setTitle(filmweb.getTitle());
        movie.setDirector(filmweb.getDirector());
        movie.setCountryOrigin(filmweb.getCountryOfOrigin());
        movie.setDuration(filmweb.getDuration());
        movie.setDescription(filmweb.getDescription());
        movie.setGenre(filmweb.getGenre());
        movie.setPosterUrl(filmweb.getPosterUrl());
        model.addAttribute("movie",movie);
        return "movies/addForm";
    }


}
