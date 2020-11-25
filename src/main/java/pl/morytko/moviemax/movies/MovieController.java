package pl.morytko.moviemax.movies;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping("/list")
    public String showMovieList(Model model){
        model.addAttribute("movies", movieService.getMovies());
        return "admin/movies/movieList";
    }

    @GetMapping("/details/{id}")
    public String showMovieDetails(@PathVariable long id, Model model){
        model.addAttribute("movie", movieService.getMovieById(id).get());
        return "admin/movies/movieDetails";
    }

    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("movie",new Movie());
        return "admin/movies/movieAddForm";
    }

    @PostMapping("/add")
    public String addMovie(@Valid Movie movie, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "admin/movies/movieAddForm";
        }
        movieService.addMovie(movie);
        return "redirect:/movies/list";
    }

    @RequestMapping("/filmweb")
    public String fillAddForm(@RequestParam String filmwebUrl, Model model) {
        Movie movie = new Movie();
        Filmweb filmweb;
        try {
            filmweb = new Filmweb(filmwebUrl);
        }catch (IOException ioException){
            model.addAttribute("errorLoadingFilmweb","true");
            return "admin/movies/movieAddForm";
        }
        movie.setTitle(filmweb.getTitle());
        movie.setDirector(filmweb.getDirector());
        movie.setCountryOrigin(filmweb.getCountryOfOrigin());
        movie.setDuration(filmweb.getDuration());
        movie.setDescription(filmweb.getDescription());
        movie.setGenre(filmweb.getGenre());
        movie.setPosterUrl(filmweb.getPosterUrl());
        model.addAttribute("movie",movie);
        return "admin/movies/movieAddForm";
    }


}
