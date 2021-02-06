package pl.morytko.moviemax.events;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.morytko.moviemax.auditoriums.Auditorium;
import pl.morytko.moviemax.auditoriums.AuditoriumService;
import pl.morytko.moviemax.cinemas.Cinema;
import pl.morytko.moviemax.cinemas.CinemaService;
import pl.morytko.moviemax.movies.Movie;
import pl.morytko.moviemax.movies.MovieService;
import pl.morytko.moviemax.screenings.Screening;
import pl.morytko.moviemax.screenings.ScreeningService;
import pl.morytko.moviemax.seats.Seat;
import pl.morytko.moviemax.seats.SeatRepository;
import pl.morytko.moviemax.users.User;
import pl.morytko.moviemax.users.UserRole;
import pl.morytko.moviemax.users.UserService;
import pl.morytko.moviemax.utils.DateUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static pl.morytko.moviemax.users.UserRole.ADMIN;
import static pl.morytko.moviemax.users.UserRole.USER;

@Component
@AllArgsConstructor
public class DatabaseInitialization {

    private final CinemaService cinemaService;
    private final MovieService movieService;
    private final AuditoriumService auditoriumService;
    private final SeatRepository seatRepository;
    private final ScreeningService screeningService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final Random random = new Random();


    @EventListener(ApplicationReadyEvent.class)
    public void appStart() throws InterruptedException {
        // initial test users created here
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("user@test.com");
        user1.setPassword(passwordEncoder.encode("usertest"));
        Set<UserRole> roles1 = new HashSet<>();
        roles1.add(USER);
        user1.setRoles(roles1);
        user1.setEnabled(true);
        users.add(user1);

        User user2 = new User();
        user2.setUsername("admin@test.com");
        user2.setPassword(passwordEncoder.encode("admintest"));
        Set<UserRole> roles2 = new HashSet<>();
        roles2.add(ADMIN);
        user2.setRoles(roles2);
        user2.setEnabled(true);
        users.add(user2);

        userService.addUserList(users);

        // cinemas -> auditoriums -> screenings created here
        List<Cinema> cinemaList = cinemaService.getCinemas();
        List<Movie> movieList = movieService.getMovies();
        if (cinemaList.size() < 3) {
            Thread.sleep(1000);
        }
        cinemaList = cinemaService.getCinemas();
        List<Screening> newScreenings = new ArrayList<>();
        List<LocalDate> twoWeeks = DateUtil.getTwoWeeks();
        cinemaList.forEach(cinema -> {
            for (int h = 0; h < 3; h++) {
                Auditorium newAuditorium = new Auditorium();
                int seatsInARow = random.nextInt(20 - 16) + 16;
                newAuditorium.setSeatNumberCount(seatsInARow);
                int rows = random.nextInt(13 - 5) + 5;
                newAuditorium.setSeatRowCount(rows);
                int screenStart = random.nextInt(4 - 2) + 2;
                newAuditorium.setScreenStart(screenStart);
                newAuditorium.setScreenEnd(seatsInARow - screenStart + 1);
                newAuditorium.setCinema(cinema);
                Auditorium savedAuditorium = auditoriumService.addAuditorium(newAuditorium);
                twoWeeks.forEach(day -> {
                    List<LocalTime> timesList = new LinkedList<>(Arrays.asList(
                            LocalTime.of(11, 5),
                            LocalTime.of(16, 30),
                            LocalTime.of(21, 0)
                    ));
                    for (int i = 0; i < 3; i++) {
                        Movie movie = movieList.get(random.nextInt(movieList.size()));
                        Screening screening = new Screening();
                        LocalTime screeningTime;
                        screeningTime = timesList.get(i);
                        screening.setScreeningDate(day);
                        screening.setScreeningTime(screeningTime);
                        screening.setAuditorium(savedAuditorium);
                        screening.setMovie(movie);
                        newScreenings.add(screening);
                    }
                });
                screeningService.saveAll(newScreenings);

                List<Seat> seatList = new ArrayList<>();
                int firstStairs = 3;
                int secondStairs = seatsInARow - 2;
                for (int i = 1; i <= rows; i++) {
                    for (int j = 1; j <= seatsInARow; j++) {
                        Seat newSeat = new Seat();
                        newSeat.setRow(i);
                        newSeat.setNumber(j);
                        if ((j == firstStairs || j == secondStairs) && i != rows) {
                            newSeat.setStatus((short) 2);
                        } else {
                            newSeat.setStatus((short) 1);
                        }
                        newSeat.setAuditorium(savedAuditorium);
                        seatList.add(newSeat);
                    }
                }
                seatRepository.saveAll(seatList);
            }
        });
    }
}
