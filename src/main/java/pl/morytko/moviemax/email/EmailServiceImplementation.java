package pl.morytko.moviemax.email;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.morytko.moviemax.reservations.Reservation;

@Service
@RequiredArgsConstructor
public class EmailServiceImplementation implements EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String SENDER_MAIL_USERNAME;

    @Override
    public void sendSimpleMessage(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER_MAIL_USERNAME);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendReservationDetails(String to, Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER_MAIL_USERNAME);
        message.setTo(to);
        message.setSubject("Twoja rezerwacja w MovieMaxCinemas na film - "+reservation.getScreening().getMovie().getTitle());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Dziękujęmy za wybranie MovieMaxCinemas!")
                .append("\n")
                .append("Oto Twoja rezerwacja:")
                .append("\n")
                .append("Numer rezerwacji: ").append(reservation.getId())
                .append("\n")
                .append("Data seansu: ").append(reservation.getScreening().getScreeningDate())
                .append("\n")
                .append("Godzina seansu: ").append(reservation.getScreening().getScreeningTime())
                .append("\n")
                .append("Miejsca: ")
                .append("\n");
        reservation.getReservedSeats().forEach(
                reservedSeat ->
                stringBuilder.append("Rząd: ")
                        .append(reservedSeat.getRow())
                        .append(" Miejsce: ")
                        .append(reservedSeat.getNumber())
                        .append("\n")
        );
        stringBuilder
                .append("Adres kina: ")
                .append("\n")
                .append(reservation.getScreening().getAuditorium().getCinema().getCity())
                .append(" - ")
                .append(reservation.getScreening().getAuditorium().getCinema().getStreet())
                .append("\n")
                .append("\n")
                .append("Do zobaczenia na miejscu!");
        message.setText(stringBuilder.toString());
        mailSender.send(message);
    }
}
