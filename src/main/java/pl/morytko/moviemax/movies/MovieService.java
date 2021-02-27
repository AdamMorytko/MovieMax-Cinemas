package pl.morytko.moviemax.movies;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> getMovies();
    Optional<Movie> getMovieById(long id);
    void addMovie(Movie movie);
    void addMovies(List<Movie> movieList);
    void updateMovie(Movie movie);
    void deleteMovie(long id);
}
