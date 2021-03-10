package pl.morytko.moviemax.email;

import pl.morytko.moviemax.reservations.Reservation;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String content);
    void sendReservationDetails(String to, Reservation reservation);
}
