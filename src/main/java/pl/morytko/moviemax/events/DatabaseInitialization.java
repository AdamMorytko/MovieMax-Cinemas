//package pl.morytko.moviemax.events;
//
//import lombok.AllArgsConstructor;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.format.datetime.DateFormatter;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import pl.morytko.moviemax.auditoriums.Auditorium;
//import pl.morytko.moviemax.auditoriums.AuditoriumService;
//import pl.morytko.moviemax.cinemas.Cinema;
//import pl.morytko.moviemax.cinemas.CinemaService;
//import pl.morytko.moviemax.movies.Movie;
//import pl.morytko.moviemax.movies.MovieService;
//import pl.morytko.moviemax.reservations.Reservation;
//import pl.morytko.moviemax.reservations.ReservationService;
//import pl.morytko.moviemax.reservedSeats.ReservedSeat;
//import pl.morytko.moviemax.reservedSeats.ReservedSeatService;
//import pl.morytko.moviemax.screenings.Screening;
//import pl.morytko.moviemax.screenings.ScreeningService;
//import pl.morytko.moviemax.seats.Seat;
//import pl.morytko.moviemax.seats.SeatRepository;
//import pl.morytko.moviemax.users.User;
//import pl.morytko.moviemax.users.UserRole;
//import pl.morytko.moviemax.users.UserService;
//import pl.morytko.moviemax.utils.DateUtil;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import static pl.morytko.moviemax.users.UserRole.ADMIN;
//import static pl.morytko.moviemax.users.UserRole.USER;
//
//@Component
//@AllArgsConstructor
//public class DatabaseInitialization {
//
//    private final CinemaService cinemaService;
//    private final MovieService movieService;
//    private final AuditoriumService auditoriumService;
//    private final SeatRepository seatRepository;
//    private final ScreeningService screeningService;
//    private final PasswordEncoder passwordEncoder;
//    private final UserService userService;
//    private final ReservationService reservationService;
//    private final ReservedSeatService reservedSeatService;
//    private final Random random = new Random();
//
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void appStart() throws InterruptedException {
//        // initial test users created here
//        List<User> users = new ArrayList<>();
//        User user1 = new User();
//        user1.setUsername("user@test.com");
//        user1.setPassword(passwordEncoder.encode("usertest"));
//        user1.setName("Jan");
//        user1.setSurname("Kowalski");
//        Set<UserRole> roles1 = new HashSet<>();
//        roles1.add(USER);
//        user1.setRoles(roles1);
//        user1.setEnabled(true);
//        users.add(user1);
//
//        User user2 = new User();
//        user2.setUsername("admin@test.com");
//        user2.setPassword(passwordEncoder.encode("admintest"));
//        user2.setName("Anna");
//        user2.setSurname("Nowak");
//        Set<UserRole> roles2 = new HashSet<>();
//        roles2.add(ADMIN);
//        user2.setRoles(roles2);
//        user2.setEnabled(true);
//        users.add(user2);
//
//        userService.addUserList(users);
//
//        // cinemas -> auditoriums -> screenings created here
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        List<Cinema> cinemaList = cinemaService.getCinemas();
//        List<Movie> movieList = movieService.getMovies();
//        if (cinemaList.size() < 3) {
//            Thread.sleep(1000);
//        }
//        cinemaList = cinemaService.getCinemas();
//        List<Screening> newScreenings = new ArrayList<>();
//        List<LocalDate> twoWeeks = DateUtil.getTwoWeeks();
//        cinemaList.forEach(cinema -> {
//            for (int h = 0; h < 3; h++) {
//                Auditorium newAuditorium = new Auditorium();
//                int seatsInARow = random.nextInt(20 - 16) + 16;
//                newAuditorium.setSeatNumberCount(seatsInARow);
//                int rows = random.nextInt(13 - 5) + 5;
//                newAuditorium.setSeatRowCount(rows);
//                int screenStart = random.nextInt(4 - 2) + 2;
//                newAuditorium.setScreenStart(screenStart);
//                newAuditorium.setScreenEnd(seatsInARow - screenStart + 1);
//                newAuditorium.setCinema(cinema);
//                Auditorium savedAuditorium = auditoriumService.addAuditorium(newAuditorium);
//                System.out.printf("INSERT INTO auditorium (seat_number_count, seat_row_count, screen_start, screen_end, cinema_id) VALUES (%d, %d, %d, %d, %d);%n",
//                        newAuditorium.getSeatNumberCount(),newAuditorium.getSeatRowCount(),newAuditorium.getScreenStart(),newAuditorium.getScreenEnd(),newAuditorium.getCinema().getId());
//                AtomicInteger atomicInteger = new AtomicInteger(0);
//                twoWeeks.forEach(day -> {
//                    List<LocalTime> timesList = new LinkedList<>(Arrays.asList(
//                            LocalTime.of(11, 5),
//                            LocalTime.of(16, 30),
//                            LocalTime.of(21, 0)
//                    ));
//                    for (int i = 0; i < 3; i++) {
//                        Movie movie = movieList.get(random.nextInt(movieList.size()));
//                        Screening screening = new Screening();
//                        LocalTime screeningTime;
//                        screeningTime = timesList.get(i);
//                        screening.setScreeningDate(day);
//                        screening.setScreeningTime(screeningTime);
//                        screening.setAuditorium(savedAuditorium);
//                        screening.setMovie(movie);
//                        System.out.printf("INSERT INTO screening (screening_date, screening_time, auditorium_id, movie_id) VALUES ((SELECT CURDATE()+INTERVAL %d DAY), '%s', %d, %d);%n",
//                                atomicInteger.getAndIncrement(),screening.getScreeningTime().format(timeFormatter),screening.getAuditorium().getId(),screening.getMovie().getId());
//                        newScreenings.add(screening);
//                        if (atomicInteger.get() == 14){
//                            atomicInteger.set(0);
//                        }
//                    }
//                });
//                screeningService.saveAll(newScreenings);
//
//                List<Seat> seatList = new ArrayList<>();
//                int firstStairs = 3;
//                int secondStairs = seatsInARow - 2;
//                for (int i = 1; i <= rows; i++) {
//                    for (int j = 1; j <= seatsInARow; j++) {
//                        Seat newSeat = new Seat();
//                        newSeat.setRow(i);
//                        newSeat.setNumber(j);
//                        if ((j == firstStairs || j == secondStairs) && i != rows) {
//                            newSeat.setStatus((short) 2);
//                        } else {
//                            newSeat.setStatus((short) 1);
//                        }
//                        newSeat.setAuditorium(savedAuditorium);
//                        System.out.printf("INSERT INTO seat (row, number, status, auditorium_id) VALUES (%d, %d, %d, %d);%n",
//                                newSeat.getRow(), newSeat.getNumber(), newSeat.getStatus(), newSeat.getAuditorium().getId());
//                        seatList.add(newSeat);
//                    }
//                }
//                seatRepository.saveAll(seatList);
//            }
//        });
//        for (int i = 1; i < 6; i++) {
//            Reservation reservation = new Reservation();
//            reservation.setUser(user1);
//            reservation.setScreening(screeningService.getScreeningById(i).get());
//            reservation.setReservedSeatNumber(2);
//            List<ReservedSeat> reservedSeats = new ArrayList<>();
//            ReservedSeat reservedSeat1 = new ReservedSeat();
//            ReservedSeat reservedSeat2 = new ReservedSeat();
//            reservedSeat1.setNumber(10);
//            reservedSeat1.setRow(1);
//            reservedSeat2.setNumber(11);
//            reservedSeat2.setRow(1);
//            reservedSeats.add(reservedSeat1);
//            reservedSeats.add(reservedSeat2);
//            reservation.setReservedSeats(reservedSeats);
//            Reservation savedReservation = reservationService.addReservation(reservation);
//            System.out.printf("INSERT INTO reservation (reserved_seat_number, screening_id, user_id) VALUES (%d, %d, %d);%n",
//                    reservation.getReservedSeatNumber(),reservation.getScreening().getId(),reservation.getUser().getId());
//            for (ReservedSeat reservedSeat : reservedSeats) {
//                reservedSeat.setReservation(savedReservation);
//            }
//            reservedSeatService.addReservedSeats(reservedSeats);
//            System.out.printf("INSERT INTO reserved_seat (number, row, reservation_id) VALUES (%d, %d, %d);%n",
//                    reservedSeat1.getNumber(), reservedSeat1.getRow(), reservedSeat1.getReservation().getId());
//            System.out.printf("INSERT INTO reserved_seat (number, row, reservation_id) VALUES (%d, %d, %d);%n",
//                    reservedSeat1.getNumber(), reservedSeat1.getRow(), reservedSeat1.getReservation().getId());
//        }
//    }
//}
